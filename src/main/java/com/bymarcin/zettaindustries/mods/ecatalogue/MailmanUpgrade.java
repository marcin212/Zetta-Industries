package com.bymarcin.zettaindustries.mods.ecatalogue;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import li.cil.oc.api.Network;
import li.cil.oc.api.internal.Agent;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;

import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.common.FMLCommonHandler;

import forestry.api.mail.EnumPostage;
import forestry.api.mail.ILetter;
import forestry.api.mail.IMailAddress;
import forestry.api.mail.IPostOffice;
import forestry.api.mail.IPostalState;
import forestry.api.mail.PostManager;
import forestry.mail.items.ItemStamps;

public class MailmanUpgrade extends AbstractManagedEnvironment {
	private final Agent robot;

	public MailmanUpgrade(Agent entity) {
		this.robot = entity;
		setNode(Network.newNode(this, Visibility.Network).withConnector().withComponent("mailman", Visibility.Neighbors).create());
	}

	public void getTileEntityHost() {
		robot.world().getTileEntity(new BlockPos((int) robot.xPosition(), (int) robot.yPosition(), (int) robot.zPosition()));
	}

	private IMailAddress getRecipientAddress(String name) {
		GameProfile gameProfile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getGameProfileForUsername(name);
		if (gameProfile == null)
			gameProfile = new GameProfile(new UUID(0, 0), name);
		return PostManager.postRegistry.getMailAddress(gameProfile);
	}

	private ItemStack consumePaper(int slot, boolean simulate) {
		ItemStack paper = robot.mainInventory().getStackInSlot(slot);
		if (!paper.isEmpty() && paper.getItem() == Items.PAPER) {
			if (simulate) {
				paper = paper.copy();
				paper.setCount(1);
				return paper;
			} else {
				return robot.mainInventory().decrStackSize(slot, 1);
			}
		} else {
			return null;
		}
	}

	private int getStampValue(int slot) {
		ItemStack stack = robot.mainInventory().getStackInSlot(slot);
		if (!stack.isEmpty() && stack.getItem() instanceof ItemStamps) {
			ItemStamps stamp = (ItemStamps) stack.getItem();
			return stamp.getPostage(stack).getValue();
		}
		return EnumPostage.P_0.getValue();
	}

	public int getNeededStampCount(int required, int stampValue) {
		if (stampValue > 0) {
			return (int) Math.ceil(required / (double) stampValue);
		} else {
			return -1;
		}
	}

	private boolean consumeStamps(ILetter letter, int stampSlot, boolean simulate) {
		ItemStack stack = robot.mainInventory().getStackInSlot(stampSlot);
		int stampValue = getStampValue(stampSlot);
		if (stampValue != 0 && getNeededStampCount(letter.requiredPostage(), stampValue) > 0 && getNeededStampCount(letter.requiredPostage(), stampValue) <= stack.getCount()) {
			if (simulate) {
				return true;
			} else {
				letter.addStamps(robot.mainInventory().decrStackSize(stampSlot, getNeededStampCount(letter.requiredPostage(), stampValue)));
				return true;
			}
		}
		return false;
	}

	public boolean addAtachments(ILetter letter, Arguments args) {
		for (int i = 4; i < args.count(); i++) {
			if (args.isInteger(i)) {
				int slot = args.checkInteger(i) - 1;
				if (slot < robot.mainInventory().getSizeInventory() && slot >= 0) {
					if (!robot.mainInventory().getStackInSlot(slot).isEmpty()) {
						letter.addAttachment(robot.mainInventory().decrStackSize(slot, robot.mainInventory().getStackInSlot(slot).getCount()));
					}
				}
			} else {
				return false;
			}
		}
		return true;
	}

	public Object[] error(String message) {
		return new Object[] { false, message };
	}

	@Callback(doc = "function():table")
	public Object[] getAllTrades(Context context, Arguments args) {
		IPostOffice postOffice = PostManager.postRegistry.getPostOffice(robot.world());
		return new Object[] { postOffice.getActiveTradeStations(robot.world()).values() };
	}

	@Callback(doc = "function(address:string, message:string, stampSlot:number, paperSlot:number, attachmentSlots:number...):boolean")
	public Object[] sendMail(Context ctx, Arguments args) {
		IMailAddress recipient = getRecipientAddress(args.checkString(0));
		IMailAddress sender = PostManager.postRegistry.getMailAddress(robot.name());
		String message = args.checkString(1);
		int stampSlot = args.checkInteger(2) - 1;
		int paperSlot = args.checkInteger(3) - 1;

		if (recipient == null || !recipient.isValid()) {
			return error("Wrong recipient address");
		}

		if (!sender.isValid() || !PostManager.postRegistry.isValidTradeAddress(robot.world(), sender) || !PostManager.postRegistry.isAvailableTradeAddress(robot.world(), sender)) {
			return error("Name reserved. Change robot name.");
		}

		if (message.length() > 128) {
			return error("Message is too long");
		}

		if (stampSlot < 0 || stampSlot > robot.mainInventory().getSizeInventory()) {
			return error("Wrong stamp slot.");
		}

		if (paperSlot < 0 || paperSlot > robot.mainInventory().getSizeInventory()) {
			return error("Wrong paper slot.");
		}

		ILetter letter = PostManager.postRegistry.createLetter(sender, recipient);
		letter.setText(message);
		if (args.count() > letter.getSizeInventory()) {
			return error("Too much attachments.");
		}

		if (consumePaper(paperSlot, true) == null) {
			return error("Wrong item or slot paper is empty.");
		}

		if (!consumeStamps(letter, stampSlot, true)) {
			return error("Wrong item, insufficient stamp value or too small amount.");
		}

		if (!addAtachments(letter, args)) {
			for (ItemStack s : letter.getAttachments())
				giveBack(s);
			return error("Wrong attachment slots.");
		}

		consumeStamps(letter, stampSlot, false);
		ItemStack paperCache = consumePaper(paperSlot, false);
		if (paperCache == null) {
			for (ItemStack stmp : letter.getPostage()) {
				giveBack(stmp);
			}
			for (ItemStack s : letter.getAttachments())
				giveBack(s);
			return error("Wrong item or slot paper is empty.");
		}

		IPostalState state = PostManager.postRegistry.getPostOffice(robot.world()).lodgeLetter(robot.world(), PostManager.postRegistry.createLetterStack(letter), true);
		if (!state.isOk()) {
			giveBack(paperCache);
			for (ItemStack s : letter.getAttachments())
				giveBack(s);
			for (ItemStack stmp : letter.getPostage()) {
				giveBack(stmp);
			}
		}

		return new Object[] { state.isOk(), state.getDescription() };
	}

	private void giveBack(ItemStack stack) {
		if (stack != null) {
			if (!mergeItemStack(stack, 0, robot.mainInventory().getSizeInventory(), false)) {
				robot.world().spawnEntity(new EntityItem(robot.world(), robot.xPosition(), robot.yPosition() + 1, robot.zPosition(), stack));
			}
		}
	}

	private boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards) {
		boolean flag1 = false;
		int k = (backwards ? end - 1 : start);
		ItemStack itemstack1;

		if (stack.isStackable())
		{
			while (stack.getCount() > 0 && (!backwards && k < end || backwards && k >= start))
			{
				itemstack1 = robot.mainInventory().getStackInSlot(k);

				if (!robot.mainInventory().isItemValidForSlot(k, stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 != null && itemstack1.getItem() == stack.getItem() &&
						(!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) &&
						ItemStack.areItemStackTagsEqual(stack, itemstack1))
				{
					int l = itemstack1.getCount() + stack.getCount();

					if (l <= stack.getMaxStackSize() && l <= robot.mainInventory().getInventoryStackLimit()) {
						stack.setCount(0);
						itemstack1.setCount(l);
						robot.mainInventory().markDirty();
						flag1 = true;
					} else if (itemstack1.getCount() < stack.getMaxStackSize() && l < robot.mainInventory().getInventoryStackLimit()) {
						stack.shrink(stack.getMaxStackSize() - itemstack1.getCount());
						itemstack1.setCount(stack.getMaxStackSize());
						robot.mainInventory().markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}

		if (stack.getCount() > 0)
		{
			k = (backwards ? end - 1 : start);

			while (!backwards && k < end || backwards && k >= start) {
				itemstack1 = robot.mainInventory().getStackInSlot(k);

				if (!robot.mainInventory().isItemValidForSlot(k, stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 == null) {
					int l = stack.getCount();

					if (l <= robot.mainInventory().getInventoryStackLimit()) {
						robot.mainInventory().setInventorySlotContents(k, stack.copy());
						stack.setCount(0);
						robot.mainInventory().markDirty();
						flag1 = true;
						break;
					} else {
						robot.mainInventory().setInventorySlotContents(k, new ItemStack(stack.getItem(), robot.mainInventory().getInventoryStackLimit(), stack.getItemDamage()));
						stack.shrink(robot.mainInventory().getInventoryStackLimit());
						robot.mainInventory().markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}

		return flag1;
	}
}

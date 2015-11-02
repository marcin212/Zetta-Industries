package com.bymarcin.zettaindustries.mods.eawiring.connectors.items;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.connection.WireProperties;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.wires.WireBase;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.IWireCoil;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.util.IEAchievements;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Lib;
import blusunrize.immersiveengineering.common.util.Utils;

public class ItemEAWireCoil extends Item implements IWireCoil {
	IIcon[] icons;
	String[] iconsName = new String[] { ":eawires/coil_copper", ":eawires/coil_iron", ":eawires/coil_tungsten" };
	public static final int coil_copper = 0;
	public static final int coil_iron = 1;
	public static final int coil_tungsten = 2;

	public ItemEAWireCoil() {
		setHasSubtypes(true);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setUnlocalizedName("EAWireCoil");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		super.addInformation(itemStack, entityPlayer, list, par4);
			WireBase wb = (WireBase)this.getWireType(itemStack);
			WireProperties wp = wb.getWireProperties();
			list.add("Nominal usage ->");
			list.add(mods.eln.misc.Utils.plotAmpere("  Current :",wp.electricalMaximalI));
			list.add(mods.eln.misc.Utils.plotOhm("Serial resistor :", wp.getResistancePerBlock()));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName()+"."+itemstack.getItemDamage();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(Item item, CreativeTabs creativetab, List list) {
		for (int i = 0; i < icons.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}

	@Override
	public void registerIcons(IIconRegister iconRegistry) {
		icons = new IIcon[iconsName.length];
		for (int i = 0; i < iconsName.length; i++) {
			icons[i] = iconRegistry.registerIcon(ZettaIndustries.MODID + iconsName[i]);
		}
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof IImmersiveConnectable && ((IImmersiveConnectable) tileEntity).canConnect())
			{
				TargetingInfo target = new TargetingInfo(side, x, y, z);
				if (!((IImmersiveConnectable) tileEntity).canConnectCable(getWireType(stack), target) || (world.getTileEntity(x, y, z) instanceof TileEntityConnectorLV))
				{
					player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "wrongCable"));
					return false;
				}

				if (!ItemNBTHelper.hasKey(stack, "linkingPos"))
				{
					ItemNBTHelper.setIntArray(stack, "linkingPos", new int[] { world.provider.dimensionId, x, y, z });
					target.writeToNBT(stack.getTagCompound());
				}
				else
				{
					WireType type = getWireType(stack);
					int[] pos = ItemNBTHelper.getIntArray(stack, "linkingPos");
					TileEntity tileEntityLinkingPos = world.getTileEntity(pos[1], pos[2], pos[3]);
					int distance = (int) Math.ceil(Math.sqrt((pos[1] - x) * (pos[1] - x) + (pos[2] - y) * (pos[2] - y) + (pos[3] - z) * (pos[3] - z)));
					if (pos[0] != world.provider.dimensionId)
						player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "wrongDimension"));
					else if (pos[1] == x && pos[2] == y && pos[3] == z)
						player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "sameConnection"));
					else if (distance > type.getMaxLength())
						player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "tooFar"));
					else if (!(tileEntityLinkingPos instanceof IImmersiveConnectable))
						player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "invalidPoint"));
					else
					{
						IImmersiveConnectable nodeHere = (IImmersiveConnectable) tileEntity;
						IImmersiveConnectable nodeLink = (IImmersiveConnectable) tileEntityLinkingPos;
						boolean connectionExists = false;
						Set<Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(world, Utils.toCC(nodeHere));
						if (outputs != null)
							for (Connection con : outputs)
							{
								if (con.end.equals(Utils.toCC(nodeLink)))
									connectionExists = true;
							}
						if (connectionExists)
							player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "connectionExists"));
						else
						{
							Vec3 rtOff0 = nodeHere.getRaytraceOffset(nodeLink).addVector(x, y, z);
							Vec3 rtOff1 = nodeLink.getRaytraceOffset(nodeHere).addVector(pos[1], pos[2], pos[3]);
							boolean canSee = Utils.canBlocksSeeOther(world, new ChunkCoordinates(x, y, z), new ChunkCoordinates(pos[1], pos[2], pos[3]), rtOff0, rtOff1);
							if (canSee)
							{
								TargetingInfo targetLink = TargetingInfo.readFromNBT(stack.getTagCompound());
								ImmersiveNetHandler.INSTANCE.addConnection(world, Utils.toCC(nodeHere), Utils.toCC(nodeLink), distance, type);
								nodeHere.connectCable(type, targetLink);
								nodeLink.connectCable(type, target);
								IESaveData.setDirty(world.provider.dimensionId);
								player.triggerAchievement(IEAchievements.connectWire);

								if (!player.capabilities.isCreativeMode)
									stack.stackSize--;
								((TileEntity) nodeHere).markDirty();
								world.markBlockForUpdate(x, y, z);
								((TileEntity) nodeLink).markDirty();
								world.markBlockForUpdate(pos[1], pos[2], pos[3]);
							}
							else
								player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "cantSee"));
						}
					}
					ItemNBTHelper.remove(stack, "linkingPos");
					ItemNBTHelper.remove(stack, "side");
					ItemNBTHelper.remove(stack, "hitX");
					ItemNBTHelper.remove(stack, "hitY");
					ItemNBTHelper.remove(stack, "hitZ");
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public WireType getWireType(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case coil_iron:
			return WireBase.MVWire;
		case coil_tungsten:
			return WireBase.HVWire;
		case coil_copper:
		default:
			return WireBase.LVWire;
		}
	}
}

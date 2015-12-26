package com.bymarcin.zettaindustries.mods.mgc.tileentities;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.item.LightBulbItem;
import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.electricity.prefab.ElectricConductor;
import com.cout970.magneticraft.api.util.VecInt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;

public class LampSocketTileEntity extends TileEntity implements IInventory, IElectricTile {
	ItemStack lightBulb;
	int lastLightValue;
	IElectricConductor filament;
	long ticksFromStart = 0;

	public static final double HIGH_IMPEDANCE = 1E19;

	public LampSocketTileEntity() {
		filament = new ElectricConductor(this, 0, 100000000);
	}

	public int getLightValue() {
		return lastLightValue;
	}

	public void setLightValue(int light) {
		this.lastLightValue = light;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
		getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
		getWorldObj().updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
		getWorldObj().addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, 0);
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;
		filament.recache();
		filament.iterate();

		filament.setStorage(1);
		
		
		if (lightBulb == null && lastLightValue != 0) {
			lastLightValue = 0;
			markDirty();
			getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
			getWorldObj().updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
			getWorldObj().addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, 0);
			filament.setResistance(HIGH_IMPEDANCE);
		}

		
		
		if (lightBulb != null) {
			filament.drainPower(LightBulbItem.getPower(lightBulb));
			int newLightValue = lastLightValue;

			if (worldObj.getTotalWorldTime() % 20 == 0) {
				updateR();
				int basicLightValue = LightBulbItem.getLightValue(lightBulb);
				newLightValue = (int) Math.max((Math.min(filament.getVoltage() / LightBulbItem.getVoltage(lightBulb), 1) - 0.5) / 0.5 * basicLightValue, 0);
				if (newLightValue > 0) {
					LightBulbItem.applyLightBulbDamage(lightBulb);
				}

				if (LightBulbItem.getStartType(lightBulb) == LightBulbItem.START_ECO && newLightValue < 0.7 * basicLightValue) {
					newLightValue = 0;
				}

			}

			if (lastLightValue == 0 && newLightValue != 0)
				ticksFromStart = 0;
			if (newLightValue != 0)
				ticksFromStart++;

			if (LightBulbItem.getStartType(lightBulb) == LightBulbItem.START_ECO && newLightValue>0) {
				if (ticksFromStart < 20 * 3 && worldObj.rand.nextFloat() < 5. / 60.) {
					newLightValue = 1;
					worldObj.playSoundEffect(xCoord, yCoord, zCoord, ZettaIndustries.MODID + ":neon_lamp", 1f, 1f);
				} else {
					newLightValue = LightBulbItem.getLightValue(lightBulb);
				}
			}

			if (newLightValue != lastLightValue) {
				lastLightValue = newLightValue;
				markDirty();
				getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
				getWorldObj().updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
				getWorldObj().addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, 0);
			}

			if (worldObj.getTotalWorldTime() % 20 == 0 && LightBulbItem.getLightBulbDamage(lightBulb) <= 0) {
				lightBulb = null;
			}
		}

	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot == 0 ? lightBulb : null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amount) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	void updateR() {
		if (lightBulb != null) {
			double R = LightBulbItem.getVoltage(lightBulb);
			R *= R;
			R /= LightBulbItem.getPower(lightBulb);
			filament.setResistance(R);
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		lightBulb = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "inventory.lamp_socket";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return itemStack.getItem() instanceof LightBulbItem;
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("lastLightValue", lastLightValue);
		if (lightBulb != null) {
			NBTTagCompound tag = new NBTTagCompound();
			lightBulb.writeToNBT(tag);
			tagCompound.setTag("lightBulb", tag);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		lastLightValue = tagCompound.getInteger("lastLightValue");
		if (tagCompound.hasKey("lightBulb")) {
			NBTTagCompound tag = tagCompound.getCompoundTag("lightBulb");
			lightBulb = ItemStack.loadItemStackFromNBT(tag);
		}
	}

	@Override
	public IElectricConductor[] getConds(VecInt arg0, int arg1) {
		return filament != null ? new IElectricConductor[] { filament } : null;
	}

}

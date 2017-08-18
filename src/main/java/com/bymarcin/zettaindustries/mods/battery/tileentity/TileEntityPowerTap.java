package com.bymarcin.zettaindustries.mods.battery.tileentity;

import java.util.HashSet;
import java.util.Set;

import com.bymarcin.zettaindustries.mods.battery.Battery;
import com.bymarcin.zettaindustries.mods.battery.block.BlockBigBatteryPowerTap;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import com.bymarcin.zettaindustries.mods.battery.gui.PowerTapContener;
import com.bymarcin.zettaindustries.mods.battery.gui.PowerTapUpdatePacket;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.utils.WorldUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;


import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;


public class TileEntityPowerTap extends BasicRectangularMultiblockTileEntityBase implements IEnergyStorage {
	int transferMax = 0;
	int transferCurrent = 0;
	private Set<EntityPlayer> updatePlayers = new HashSet<EntityPlayer>();
	String label = "";

	public TileEntityPowerTap() {

	}

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(getMultiblockController()!=null && isOutput() && getMultiblockController().isAssembled()){
            int temp =((BatteryController)getMultiblockController()).getStorage().receiveEnergy(Math.min(maxReceive,transferCurrent), simulate);
            if(!simulate){((BatteryController)getMultiblockController()).modifyLastTickBalance(temp);}
            return temp;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(getMultiblockController()!=null && !isOutput() && getMultiblockController().isAssembled()){
            int temp = ((BatteryController)getMultiblockController()).getStorage().extractEnergy(Math.min(maxExtract,transferCurrent), simulate);
            if(!simulate){((BatteryController)getMultiblockController()).modifyLastTickBalance(-temp);}
            return temp;
        }
        return 0;
    }

    @Override
    public int getEnergyStored() {
        if(getMultiblockController()!=null){
            return ((BatteryController)getMultiblockController()).getStorage().getEnergyStored();
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        if(getMultiblockController()!=null){
            return ((BatteryController)getMultiblockController()).getStorage().getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    public boolean canExtract() {
        return !isOutput();
    }

    @Override
    public boolean canReceive() {
        return isOutput();
    }

    @Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == CapabilityEnergy.ENERGY) || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
	}

	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void beginUpdatingPlayer(EntityPlayer playerToUpdate) {
		updatePlayers.add(playerToUpdate);
		sendIndividualUpdate(playerToUpdate);
	}	

	protected void sendIndividualUpdate(EntityPlayer player) {
		if(this.getWorld().isRemote) { return; }
		ZIRegistry.packetHandler.sendTo(getUpdatePowerTapPacket(), (EntityPlayerMP) player);
	}
	
	protected PowerTapUpdatePacket getUpdatePowerTapPacket(){
	     return new PowerTapUpdatePacket(this,transferCurrent,true,getLabel());
	}


	public void stopUpdatingPlayer(EntityPlayer playerToRemove) {
		updatePlayers.remove(playerToRemove);
	}
	
	public int getTransferCurrent(){
		return transferCurrent;
	}
	
	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Power tap may not be placed in the battery's frame", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Power tap may not be placed in the battery's sides", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		TileEntity entityBelow = this.getWorld().getTileEntity(this.getPos().down());
		if ((entityBelow instanceof TileEntityElectrode)) {
			return;
		}
		throw new MultiblockValidationException(String.format("%d, %d, %d - Power tap must be placed on electrode", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Power tap may not be placed in the battery's bottom", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Power tap may not be placed in the battery's interior", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
		super.onMachineAssembled(controller);
		transferMax = 0;
		int i = 1;
		Block b;
		while (true) {
			b = getWorld().getBlockState(new BlockPos(this.getPos().getX(), this.getPos().getY() - i, this.getPos().getZ())).getBlock();
			if (b != Battery.blockBigBatteryElectrode) {
				break;
			}

			for (EnumFacing d : EnumFacing.VALUES) {
				if(EnumFacing.UP==d) continue;
				if (BatteryController.checkElectrolyte(getWorld(), getPos().getX() + d.getDirectionVec().getX(), getPos().getY() + d.getDirectionVec().getY() - i, getPos().getZ() + d.getDirectionVec().getZ()) != 0) {
					transferMax += Battery.electrodeTransferRate;
				}
			}
			i++;
		}

		if(transferCurrent > transferMax)
			transferCurrent=transferMax;
	}

	@Override
	public void onMachineActivated() {
	}

	@Override
	public void onMachineDeactivated() {
	}


	
	public int onTransferEnergy(){
			if(WorldUtils.isClientWorld(getWorld()) || isOutput() || getMultiblockController()==null) return 0;
			TileEntity tile = WorldUtils.getAdjacentTileEntity(getWorld(), getPos(), EnumFacing.UP);
			int energyGet=0;
		    if(tile!= null && tile.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)) {
                IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
                energyGet = energyStorage.receiveEnergy(Math.min(transferCurrent, ((BatteryController)getMultiblockController()).getStorage().getEnergyStored()), false);
			}
			((BatteryController)getMultiblockController()).getStorage().modifyEnergyStored(-energyGet);
			return energyGet;
	}

	public void setTransfer(int transfer){
		if(getWorld().isRemote){
			transferCurrent = transfer;
		}else{
			transferCurrent = Math.max(0,Math.min(transfer, transferMax));	
		}
	}
	
	public void updatePowerTap(){
		for(EntityPlayer p: updatePlayers)
			ZIRegistry.packetHandler.sendTo(getUpdatePowerTapPacket(), (EntityPlayerMP) p);
	}
	
	public Container getContainer(EntityPlayer player){
		return new PowerTapContener(this, player);
	}
	
	public boolean isOutput() {
		return getBlockMetadata()==0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		transferCurrent = data.getInteger("transfer");
		if(data.hasKey("powertap_label")){
			label = data.getString("powertap_label");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		data.setInteger("transfer",transferCurrent);
		data.setString("powertap_label", label);
		return data;
	}
	
	public void setIn(){
		getWorld().setBlockState(getPos(),getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryPowerTap.IO, BlockBigBatteryPowerTap.INPUT),2);
	}
	
	public void setOut(){
		getWorld().setBlockState(getPos(),getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryPowerTap.IO, BlockBigBatteryPowerTap.OUTPUT),2);
	}
}

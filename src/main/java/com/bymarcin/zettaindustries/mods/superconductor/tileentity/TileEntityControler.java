package com.bymarcin.zettaindustries.mods.superconductor.tileentity;

import java.util.ArrayList;

import com.bymarcin.zettaindustries.utils.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import cofh.api.energy.IEnergyHandler;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.common.CoordTriplet;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;
import com.bymarcin.zettaindustries.mods.superconductor.gui.PacketUpdateFluidAmount;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.network.Packet;
import com.bymarcin.zettaindustries.utils.WorldUtils;

public class TileEntityControler extends TileEntityBase implements
		IEnergyHandler, IFluidHandler {
	ArrayList<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();

	
	public int onReciveEnergy(int maxRecive, boolean simulate){
		if (WorldUtils.isClientWorld(worldObj) || !isOutput()) return 0;
		ArrayList<Pair<Integer,Pair<ForgeDirection,IEnergyHandler>>> order = new ArrayList<Pair<Integer,Pair<ForgeDirection,IEnergyHandler>>>();
		int tempOrder;
		long requiredEnergy = 0;
		double ratio;
		int energyConsumed=0;
		TileEntity tempTile;
		for(ForgeDirection d: ForgeDirection.VALID_DIRECTIONS){
			tempTile = WorldUtils.getAdjacentTileEntity(this, d);
			if(getMultiblockController()==null || tempTile==null || getMultiblockController().hasBlock(new CoordTriplet(tempTile.xCoord,tempTile.yCoord,tempTile.zCoord))) continue;
			if (WorldUtils.isEnergyHandlerFromSide(tempTile, ForgeDirection.VALID_DIRECTIONS[(d.ordinal() ^ 0x1)])) {
				tempOrder = ((IEnergyHandler) tempTile).receiveEnergy(ForgeDirection.VALID_DIRECTIONS[(d.ordinal() ^ 0x1)], Integer.MAX_VALUE, true);
				order.add(new Pair<Integer,Pair<ForgeDirection,IEnergyHandler>>(tempOrder, new Pair<ForgeDirection,IEnergyHandler>(ForgeDirection.VALID_DIRECTIONS[(d.ordinal() ^ 0x1)],(IEnergyHandler) tempTile)));
				requiredEnergy+=tempOrder;
			}
		}
		ratio = (double)maxRecive/(double)requiredEnergy;
		
		for(Pair<Integer,Pair<ForgeDirection,IEnergyHandler>> con: order){
			energyConsumed += con.getValue().getValue().receiveEnergy( con.getValue().getKey(), (int)Math.floor(con.getKey()*ratio), simulate);
		}

		return energyConsumed;
	}
	
	@SuppressWarnings("rawtypes")
	protected Packet getUpdatePacket(){
	     return new PacketUpdateFluidAmount(this, getTank().getFluidAmount(), ((getTank().getFluid()!=null)?getTank().getFluid().fluidID:0));
	}
	
	public void updateTick(){
		for(EntityPlayerMP p  : players)
			ZIRegistry.packetHandler.sendTo(getUpdatePacket(), p);
	}
	
	public void addPlayerToUpdate(EntityPlayerMP p){
		players.add(p);
		if(WorldUtils.isServerWorld(worldObj))
			ZIRegistry.packetHandler.sendTo(getUpdatePacket(), p);
	}
	
	public void removePlayerFromUpdate(EntityPlayer p){
		players.remove(p);
	}

	public boolean isOutput() {
		return (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 1) == 0;
	}

	@Override
	public void onMachineActivated() {
		if(getMultiblockController()==null) return;
		((SuperConductor)getMultiblockController()).validateAllControlers();
	}

	@Override
	public void onMachineAssembled(MultiblockControllerBase arg0) {
			((SuperConductor)arg0).validateAllControlers();
	}

	@Override
	public void onMachineBroken() {
	
	}

	@Override
	public void onMachineDeactivated() {

	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if(getMultiblockController() !=null){
			return isOutput()?0:((SuperConductor)getMultiblockController()).onReciveEnergy(maxReceive, simulate);
		}else return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return Integer.MAX_VALUE;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
	}

	FluidTank getTank(){
		SuperConductor c = (SuperConductor) getMultiblockController();
		return c!=null?c.getTank():null;
	}
	
	/* IFluidHandler */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
    	if(resource.fluidID == FluidRegistry.getFluidID("cryotheum") && getTank()!=null)
    		return getTank().fill(resource, doFill);
    	else
    		return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (resource == null || !resource.isFluidEqual(getTank().getFluid()))
        {
            return null;
        }
        return getTank().drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return getTank().drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {	
    	if(getTank()!=null)
    		return new FluidTankInfo[] { getTank().getInfo() };
    	else
    		return null;
    }


	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}
}

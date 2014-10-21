package com.bymarcin.zettaindustries.mods.superconductor.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockTileEntityBase;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductorMod;

public abstract class TileEntityBase extends MultiblockTileEntityBase{
	
	FluidTank coolantTank = new FluidTank(4000);
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase multiblockControllerBase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMachineBroken() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMachineActivated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMachineDeactivated() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public SuperConductor getMultiblockController() {
		return (SuperConductor) super.getMultiblockController();
	}
	
	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new SuperConductor(worldObj);
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return SuperConductor.class;
	}
	
	public int getCoolantAmount(){
		return coolantTank.getFluidAmount();
	}
	
	public int getCoolantCapacity(){
		return coolantTank.getCapacity();
	}
	
	public int addCoolant(int amount){
		return coolantTank.fill(new FluidStack(SuperConductorMod.coolantFluid, amount), true);
	}
	
	public int drainCoolant(int toDrain){
		FluidStack drain = coolantTank.drain(Math.min(toDrain,getCoolantAmount()), true);
		return drain!=null?drain.amount:0;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		coolantTank.writeToNBT(data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		coolantTank.readFromNBT(data);
	}
}
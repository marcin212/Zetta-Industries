package com.bymarcin.zettaindustries.mods.superconductor.tileentity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityWire extends TileEntityBase{

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
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
	}
}

package com.bymarcin.zettaindustries.mods.battery.tileentity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;

public class TileEntityControler extends BasicRectangularMultiblockTileEntityBase{

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery frame", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {
		
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery top", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery bottom", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery interior", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
		super.onMachineAssembled(controller);
		if(this.worldObj.isRemote) return;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if(this.worldObj.isRemote) return;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
	}
}

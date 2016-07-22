package com.bymarcin.zettaindustries.mods.battery.tileentity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;

public class TileEntityGlass extends BasicRectangularMultiblockTileEntityBase{

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's frame", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {
		
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's top", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's bottom", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's interior", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
}

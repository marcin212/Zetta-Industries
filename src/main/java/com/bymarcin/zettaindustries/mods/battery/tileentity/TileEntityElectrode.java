package com.bymarcin.zettaindustries.mods.battery.tileentity;

import net.minecraft.tileentity.TileEntity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.rectangular.RectangularMultiblockTileEntityBase;

public class TileEntityElectrode extends BasicRectangularMultiblockTileEntityBase{

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		TileEntity entityAbove = this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
		if ((!(entityAbove instanceof TileEntityElectrode)) && (!(entityAbove instanceof TileEntityPowerTap))) {
			  throw new MultiblockValidationException(String.format("Electrode at %d, %d, %d must be part of a vertical column that reaches the entire height of the battery, with a power tap on top.", this.xCoord, this.yCoord, this.zCoord));
		}
		TileEntity entityBelow = this.worldObj.getTileEntity(this.xCoord,  this.yCoord - 1, this.zCoord);
		if ((entityBelow instanceof TileEntityElectrode)) {
			return;
		}
		if ((entityBelow instanceof RectangularMultiblockTileEntityBase)) {
			((RectangularMultiblockTileEntityBase)entityBelow).isGoodForBottom();
			 return;
		}
		throw new MultiblockValidationException(String.format("Electrode at %d, %d, %d must be part of a vertical column that reaches the entire height of the battery, with a power tap on top.", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
}

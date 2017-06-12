package com.bymarcin.zettaindustries.mods.battery.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityElectrode extends BasicRectangularMultiblockTileEntityBase{

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Electrode may only be placed in the battery interior", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		TileEntity entityAbove = this.worldObj.getTileEntity(this.getPos().up());
		if ((!(entityAbove instanceof TileEntityElectrode)) && (!(entityAbove instanceof TileEntityPowerTap))) {
			  throw new MultiblockValidationException(String.format("Electrode at %d, %d, %d must be part of a vertical column that spans from the top of battery, with a power tap on top.", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
		}
	}

	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
}

package com.bymarcin.zettaindustries.mods.battery.tileentity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityGlass extends BasicRectangularMultiblockTileEntityBase{

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's frame", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {
		
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		//throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's top", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's bottom", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Glass may not be placed in the battery's interior", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
	}

	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
}

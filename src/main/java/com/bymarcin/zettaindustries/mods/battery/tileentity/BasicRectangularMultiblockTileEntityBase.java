package com.bymarcin.zettaindustries.mods.battery.tileentity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.rectangular.RectangularMultiblockTileEntityBase;

public abstract class BasicRectangularMultiblockTileEntityBase extends RectangularMultiblockTileEntityBase{


	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new BatteryController(worldObj);
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return BatteryController.class;
	}

}

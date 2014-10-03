package com.bymarcin.zettaindustries.mods.superconductor.tileentity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockTileEntityBase;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;

public abstract class TileEntityBase extends MultiblockTileEntityBase{

	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new SuperConductor(worldObj);
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return SuperConductor.class;
	}

}

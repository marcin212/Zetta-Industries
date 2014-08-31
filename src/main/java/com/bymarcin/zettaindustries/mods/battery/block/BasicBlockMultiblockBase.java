package com.bymarcin.zettaindustries.mods.battery.block;

import net.minecraft.block.material.Material;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.BlockMultiblockBase;

public abstract class BasicBlockMultiblockBase extends BlockMultiblockBase {
	public BasicBlockMultiblockBase(String name) {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setHardness(3.0F);
		setBlockName(name);
	}
}

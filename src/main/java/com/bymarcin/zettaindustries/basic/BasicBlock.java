package com.bymarcin.zettaindustries.basic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.bymarcin.zettaindustries.ZettaIndustries;

public class BasicBlock extends Block{

	protected BasicBlock(Material material, String name) {
		super(material);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName(name);
		setHardness(3.0F);
	}

}

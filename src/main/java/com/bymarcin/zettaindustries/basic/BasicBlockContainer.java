package com.bymarcin.zettaindustries.basic;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;


public abstract class BasicBlockContainer extends BlockContainer{

	public BasicBlockContainer(Material material, String name) {
		super(material);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setRegistryName(name);
		setHardness(3.0F);
		setResistance(15.0F);
	}

}

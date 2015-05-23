package com.bymarcin.zettaindustries.basic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.bymarcin.zettaindustries.ZettaIndustries;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock extends Block implements IBlockInfo {

	protected BasicBlock(Material material, String name) {
		super(material);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName(name);
		setHardness(3.0F);
	}

    @Override
    public List<String> getInformation() {
        return new ArrayList<String>();
    }
}

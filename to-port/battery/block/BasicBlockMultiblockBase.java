package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.basic.IBlockInfo;
import net.minecraft.block.material.Material;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.BlockMultiblockBase;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicBlockMultiblockBase extends BlockMultiblockBase implements IBlockInfo {
    protected List<String> info = new ArrayList<String>();

	public BasicBlockMultiblockBase(String name) {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setHardness(3.0F);
		setBlockName(name);
	}

    @Override
    public List<String> getInformation() {
        return info;
    }

    public String localize(String key) {
        return StatCollector.canTranslate(key) ? StatCollector.translateToLocal(key) : StatCollector.translateToFallback(key);
    }

}

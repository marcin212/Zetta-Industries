package com.bymarcin.zettaindustries.basic;

import java.util.ArrayList;
import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.text.translation.I18n;

public class BasicBlock extends Block implements IBlockInfo {

	protected BasicBlock(Material material, String name) {
		super(material);
		setCreativeTab(ZettaIndustries.tabZettaIndustries);
		setRegistryName(name);
		setUnlocalizedName(ZettaIndustries.MODID + "." + name);
		setHardness(3.0F);
		
	}

    @Override
    public List<String> getInformation() {
        return new ArrayList<String>();
    }

    public String localize(String key) {
        return I18n.canTranslate(key) ? I18n.translateToLocal(key) : I18n.translateToFallback(key);
    }

}

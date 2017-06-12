package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.basic.IBlockInfo;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.BlockMultiblockBase;
import net.minecraft.block.material.Material;

import com.bymarcin.zettaindustries.ZettaIndustries;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.text.translation.I18n;


import java.util.ArrayList;
import java.util.List;

public abstract class BasicBlockMultiblockBase extends BlockMultiblockBase implements IBlockInfo {
    protected List<String> info = new ArrayList<String>();

	public BasicBlockMultiblockBase(String name) {
		this(name, Material.IRON);
	}

    public BasicBlockMultiblockBase(String name, Material material) {
        super(material);
        setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
        setHardness(3.0F);
        setRegistryName(name);
        setUnlocalizedName(name);
    }

    @Override
    public List<String> getInformation() {
        return info;
    }

    public String localize(String key) {
        return I18n.canTranslate(key) ? I18n.translateToLocal(key) : I18n.translateToFallback(key);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}

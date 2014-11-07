package com.bymarcin.zettaindustries.mods.battery.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlock;

public class BlockGraphite extends BasicBlock{

	public BlockGraphite() {
		super(Material.rock, "blockGraphite");
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegistrer) {
		blockIcon = iconRegistrer.registerIcon(ZettaIndustries.MODID+":battery/blockGraphite");
	}

}

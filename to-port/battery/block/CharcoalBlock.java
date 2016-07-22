package com.bymarcin.zettaindustries.mods.battery.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CharcoalBlock extends BasicBlock {

	public CharcoalBlock() {
		super(Material.iron, "charcoalblock");
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(Block.soundTypeStone);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		blockIcon = registry.registerIcon(ZettaIndustries.MODID+":charcoal_block");
	}
}

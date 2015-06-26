package com.bymarcin.zettaindustries.mods.quarryfixer;

import net.minecraft.init.Blocks;

import net.minecraftforge.oredict.ShapedOreRecipe;

import com.bymarcin.zettaindustries.modmanager.IMod;

import cpw.mods.fml.common.registry.GameRegistry;

public class QuarryFixer implements IMod{
	static QuarryFixerBlock quarryfixerblock;
	
	@Override
	public void init() {
		quarryfixerblock = new QuarryFixerBlock();
		GameRegistry.registerBlock(quarryfixerblock,"quarryfixerblock");
		 GameRegistry.addRecipe(new ShapedOreRecipe(quarryfixerblock, " x ", "xdx", " x ", 
				 'x', "gearIron", 'd', Blocks.dispenser));
	}

	@Override
	public void postInit() {

	}
	
	@Override
	public void preInit() {

	}
}

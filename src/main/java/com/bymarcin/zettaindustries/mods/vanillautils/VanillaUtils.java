package com.bymarcin.zettaindustries.mods.vanillautils;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.vanillautils.block.VariableRedstoneEmitter;

import cpw.mods.fml.common.registry.GameRegistry;

public class VanillaUtils implements IMod{

	public static VariableRedstoneEmitter variableredstoneemitter;

	
	@Override
	public void init() {
		
		/*Variable Redstone Emitter*/
		if(ZettaIndustries.instance.config.get("VanillaUtils", "VariableRedstoneEmitter", true).getBoolean(true)){
			variableredstoneemitter = new VariableRedstoneEmitter();
			GameRegistry.registerBlock(variableredstoneemitter, "variableredstoneemitter");
			GameRegistry.addRecipe(new ItemStack(variableredstoneemitter), "   ", "rzr", " x ",
				 'x', Items.repeater, 'r', Items.redstone, 'z', Blocks.redstone_torch);
		}

	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
	}

}

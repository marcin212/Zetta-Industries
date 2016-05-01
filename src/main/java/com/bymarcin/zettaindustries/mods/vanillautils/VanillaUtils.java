package com.bymarcin.zettaindustries.mods.vanillautils;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.vanillautils.block.VariableRedstoneEmitter;


public class VanillaUtils implements IMod{
	public static VariableRedstoneEmitter variableredstoneemitter;

	@Override
	public void init() {
		
	}

	@Override 
	public void postInit() {

	}

	@Override
	public void preInit() {
		/*Variable Redstone Emitter*/
		if(ZettaIndustries.instance.config.get("VanillaUtils", "VariableRedstoneEmitter", true).getBoolean(true)){
			ZettaIndustries.logger.info("register: variableredstoneemitter");
			variableredstoneemitter = GameRegistry.register(new VariableRedstoneEmitter());
			Item i = GameRegistry.register(new ItemBlock(variableredstoneemitter).setRegistryName(variableredstoneemitter.getRegistryName()));
			ZettaIndustries.proxy.registermodel(i, 0);
			GameRegistry.addRecipe(new ItemStack(variableredstoneemitter), "   ", "rzr", " x ",
				 'x', Items.REPEATER, 'r', Items.REDSTONE, 'z', Blocks.REDSTONE_TORCH);
		}
	}

}

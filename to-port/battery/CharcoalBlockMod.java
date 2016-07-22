package com.bymarcin.zettaindustries.mods.battery;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.battery.block.CharcoalBlock;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class CharcoalBlockMod implements IMod{
	public static CharcoalBlock charcoalblock;
	ItemStack coal;
	ItemStack coalx9;
	
	@Override
	public void preInit() {

	}
	
	@Override
	public void init() {
		charcoalblock = new CharcoalBlock();
		GameRegistry.registerBlock(charcoalblock, "charcoalblock");
		GameRegistry.registerFuelHandler(new CharcoalFuelHandler());
	}

	@Override
	public void postInit() {
		coal = new ItemStack(Items.coal, 1,1);
        coalx9 = new ItemStack(Items.coal, 9,1);
		GameRegistry.addShapelessRecipe(new ItemStack(charcoalblock),
				coal,coal,coal,
				coal,coal,coal,
				coal,coal,coal);
		GameRegistry.addShapelessRecipe(coalx9, charcoalblock);
		
		
	}

}

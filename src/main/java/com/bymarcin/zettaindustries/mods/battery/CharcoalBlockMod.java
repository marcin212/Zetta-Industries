package com.bymarcin.zettaindustries.mods.battery;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.battery.block.CharcoalBlock;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CharcoalBlockMod implements IMod{
	public static CharcoalBlock charcoalblock;
	ItemStack coal;
	ItemStack coalx9;
	
	@Override
	public void preInit() {
		charcoalblock = new CharcoalBlock();
		Item itemCharcoalblock = GameRegistry.register(new ItemBlock(GameRegistry.register(charcoalblock)).setRegistryName(charcoalblock.getRegistryName()));
		ZettaIndustries.proxy.registermodel(itemCharcoalblock,0);
		GameRegistry.registerFuelHandler(new CharcoalFuelHandler());
	}
	
	@Override
	public void init() {

	}

	@Override
	public void postInit() {
		coal = new ItemStack(Items.COAL, 1,1);
        coalx9 = new ItemStack(Items.COAL, 9,1);
		GameRegistry.addShapelessRecipe(new ItemStack(charcoalblock),
				coal,coal,coal,
				coal,coal,coal,
				coal,coal,coal);
		GameRegistry.addShapelessRecipe(coalx9, charcoalblock);
		
		
	}

}

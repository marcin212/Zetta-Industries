package com.bymarcin.zettaindustries.mods.forestrybackpacks;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.BasicBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.CreativeBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.ImmersiveEngineeringBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.OCBackpack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ItemStackHolder;

import forestry.api.recipes.RecipeManagers;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.EnumBackpackType;
import forestry.core.config.ForestryItem;
import forestry.core.fluids.Fluids;

public class ForestyBackpacksMod implements IMod{
	
	static Item creativeBackpackT1;
	static Item creativeBackpackT2; 
	
	static Item immersiveEngineeringBackpackT1;
	static Item immersiveEngineeringBackpackT2;
	
	static Item OCBackpackT1;
	static Item OCBackpackT2;
	
	@ItemStackHolder(value="OpenComputers:item", meta=96)
	public static final ItemStack oc = null;
	
	@ItemStackHolder(value="ImmersiveEngineering:material", meta=4)
	public static final ItemStack ie = null;
	
	@Override
	public void preInit() {
		creativeBackpackT1 = addBackpack(new CreativeBackpack(EnumBackpackType.T1), EnumBackpackType.T1);
		creativeBackpackT2 = addBackpack(new CreativeBackpack(EnumBackpackType.T2), EnumBackpackType.T2);
		
		immersiveEngineeringBackpackT1 = addBackpack(new ImmersiveEngineeringBackpack(EnumBackpackType.T1), EnumBackpackType.T1);
		immersiveEngineeringBackpackT2 = addBackpack(new ImmersiveEngineeringBackpack(EnumBackpackType.T2), EnumBackpackType.T2);
		
		OCBackpackT1 = addBackpack(new OCBackpack(EnumBackpackType.T1), EnumBackpackType.T1);
		OCBackpackT2 = addBackpack(new OCBackpack(EnumBackpackType.T2), EnumBackpackType.T2);
	}

	@Override
	public void init() {
		
	}

	@Override
	public void postInit() {
		
		if(ie!=null)
			addRecipe(immersiveEngineeringBackpackT1, immersiveEngineeringBackpackT2, ie);
		if(oc!=null)
			addRecipe(OCBackpackT1, OCBackpackT2, oc);
		
	}
	
	private Item addBackpack(BasicBackpack backpack, EnumBackpackType type){
		Item backpakItem = BackpackManager.backpackInterface.addBackpack(backpack, type);
		GameRegistry.registerItem(backpakItem,backpack.getKey());
		backpakItem.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		return backpakItem;
	}
	
	private void addRecipe(Item backpackT1, Item backpackT2, ItemStack crafting){
		RecipeManagers.carpenterManager.addRecipe(200, Fluids.WATER.getFluid(1000), null, new ItemStack(backpackT2), "WXW", "WTW", "WWW", 'X', Items.diamond, 'W',
				ForestryItem.craftingMaterial.getItemStack(1, 3), 'T', backpackT1);
		GameRegistry.addShapedRecipe(new ItemStack(backpackT1), "X#X", "VYV", "X#X", '#', Blocks.wool,
				'X', Items.string, 'V', crafting, 'Y', Blocks.chest);
	}

}

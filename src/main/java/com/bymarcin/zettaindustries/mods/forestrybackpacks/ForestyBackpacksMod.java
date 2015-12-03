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

import forestry.api.recipes.RecipeManagers;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.EnumBackpackType;
import forestry.core.fluids.Fluids;
import forestry.plugins.PluginApiculture;
import forestry.plugins.PluginCore;
import forestry.plugins.PluginMail;

public class ForestyBackpacksMod implements IMod {

	static Item creativeBackpackT1;
	static Item creativeBackpackT2;

	static Item immersiveEngineeringBackpackT1;
	static Item immersiveEngineeringBackpackT2;

	static Item OCBackpackT1;
	static Item OCBackpackT2;

	public static ItemStack oc = null;
	public static ItemStack ie = null;
	
	boolean preinit = false;

	@Override
	public void preInit() {
		if (BackpackManager.backpackInterface == null) return;
			creativeBackpackT1 = addBackpack(new CreativeBackpack(EnumBackpackType.T1), EnumBackpackType.T1);
			creativeBackpackT2 = addBackpack(new CreativeBackpack(EnumBackpackType.T2), EnumBackpackType.T2);

			immersiveEngineeringBackpackT1 = addBackpack(new ImmersiveEngineeringBackpack(EnumBackpackType.T1), EnumBackpackType.T1);
			immersiveEngineeringBackpackT2 = addBackpack(new ImmersiveEngineeringBackpack(EnumBackpackType.T2), EnumBackpackType.T2);

			OCBackpackT1 = addBackpack(new OCBackpack(EnumBackpackType.T1), EnumBackpackType.T1);
			OCBackpackT2 = addBackpack(new OCBackpack(EnumBackpackType.T2), EnumBackpackType.T2);
			preinit = true;
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {
		if (!preinit) return;
		ie = GameRegistry.findItemStack("ImmersiveEngineering", "material", 1);
		oc = GameRegistry.findItemStack("OpenComputers", "item", 1);

		if (ie != null) {
			ie.setItemDamage(4);
			addRecipe(immersiveEngineeringBackpackT1, immersiveEngineeringBackpackT2, ie);
		}

		if (oc != null) {
			oc.setItemDamage(96);
			addRecipe(OCBackpackT1, OCBackpackT2, oc);
		}

	}

	private Item addBackpack(BasicBackpack backpack, EnumBackpackType type) {
		Item backpakItem = BackpackManager.backpackInterface.addBackpack(backpack, type);
		GameRegistry.registerItem(backpakItem, backpack.getKey());
		backpakItem.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		return backpakItem;
	}

	private void addRecipe(Item backpackT1, Item backpackT2, ItemStack crafting) {
		RecipeManagers.carpenterManager.addRecipe(200, Fluids.WATER.getFluid(1000), null, new ItemStack(backpackT2), "WXW", "WTW", "WWW", 'X', Items.diamond, 'W',
				PluginCore.items.craftingMaterial.getWovenSilk(), 'T', backpackT1);
		GameRegistry.addShapedRecipe(new ItemStack(backpackT1), "X#X", "VYV", "X#X", '#', Blocks.wool,
				'X', Items.string, 'V', crafting, 'Y', Blocks.chest);
	}

}

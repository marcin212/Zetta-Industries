package com.bymarcin.zettaindustries.mods.forestrybackpacks;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.BasicBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.CreativeBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.ImmersiveEngineeringBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.OCBackpack;

import forestry.core.PluginCore;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import forestry.api.recipes.RecipeManagers;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.EnumBackpackType;
import forestry.core.fluids.Fluids;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
			creativeBackpackT1 = addBackpack(new CreativeBackpack(EnumBackpackType.NORMAL), EnumBackpackType.NORMAL);
			creativeBackpackT2 = addBackpack(new CreativeBackpack(EnumBackpackType.WOVEN), EnumBackpackType.WOVEN);

			immersiveEngineeringBackpackT1 = addBackpack(new ImmersiveEngineeringBackpack(EnumBackpackType.NORMAL), EnumBackpackType.NORMAL);
			immersiveEngineeringBackpackT2 = addBackpack(new ImmersiveEngineeringBackpack(EnumBackpackType.WOVEN), EnumBackpackType.WOVEN);

			OCBackpackT1 = addBackpack(new OCBackpack(EnumBackpackType.NORMAL), EnumBackpackType.NORMAL);
			OCBackpackT2 = addBackpack(new OCBackpack(EnumBackpackType.WOVEN), EnumBackpackType.WOVEN);
			preinit = true;
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {
		if (!preinit) return;
		ie = GameRegistry.makeItemStack("ImmersiveEngineering:material", 4, 1, "");
		oc = GameRegistry.makeItemStack("OpenComputers:item", 1, 96, "");

		if (ie != null) {
			addRecipe(immersiveEngineeringBackpackT1, immersiveEngineeringBackpackT2, ie);
		}

		if (oc != null) {
			addRecipe(OCBackpackT1, OCBackpackT2, oc);
		}

	}

	private Item addBackpack(BasicBackpack backpack, EnumBackpackType type) {
		Item backpakItem = BackpackManager.backpackInterface.createBackpack(backpack, type);
		backpakItem.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		backpakItem.setRegistryName(backpack.getKey());
		ZettaIndustries.proxy.registermodel(GameRegistry.register(backpakItem),0);
		return backpakItem;
	}

	private void addRecipe(Item backpackT1, Item backpackT2, ItemStack crafting) {
		RecipeManagers.carpenterManager.addRecipe(200, Fluids.WATER.getFluid(1000), null, new ItemStack(backpackT2), "WXW", "WTW", "WWW", 'X', Items.DIAMOND, 'W',
				PluginCore.items.craftingMaterial.getSilkWisp(), 'T', backpackT1);
		GameRegistry.addShapedRecipe(new ItemStack(backpackT1), "X#X", "VYV", "X#X", '#', Blocks.WOOL,
				'X', Items.STRING, 'V', crafting, 'Y', Blocks.CHEST);
	}

}

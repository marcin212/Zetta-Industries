package com.bymarcin.zettaindustries.mods.forestrybackpacks;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import forestry.api.recipes.RecipeManagers;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.EnumBackpackType;
import forestry.core.ModuleCore;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.BasicBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.CreativeBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.ImmersiveEngineeringBackpack;
import com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks.OCBackpack;

public class ForestyBackpacksMod implements IMod {
	private static final Set<Item> itemsToRegister = new HashSet<>();
	private static final Set<IRecipe> recipesToRegister = new HashSet<>();

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

		if (ie != null) {
			addRecipe(immersiveEngineeringBackpackT1, immersiveEngineeringBackpackT2, ie, null);
		}

		if (oc != null) {
			addRecipe(OCBackpackT1, OCBackpackT2, oc, null);
		}

	}

	@SubscribeEvent
	public void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		ie = GameRegistry.makeItemStack("ImmersiveEngineering:material", 4, 1, "");
		oc = GameRegistry.makeItemStack("opencomputers:item", 1, 96, "");

		if (ie != null) {
			addRecipe(immersiveEngineeringBackpackT1, immersiveEngineeringBackpackT2, ie, event.getRegistry());
		}

		if (oc != null) {
			addRecipe(OCBackpackT1, OCBackpackT2, oc, event.getRegistry());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onModelRegister(ModelRegistryEvent event) {
		for (Item item : itemsToRegister) {
			ZettaIndustries.proxy.registermodel(item,0);
		}
	}

	@SubscribeEvent
	public void onItemRegister(RegistryEvent.Register<Item> event) {
		for (Item item : itemsToRegister) {
			event.getRegistry().register(item);
		}
	}

	private Item addBackpack(BasicBackpack backpack, EnumBackpackType type) {
		Item backpakItem = BackpackManager.backpackInterface.createBackpack(backpack.getUniqueName(), type);
		backpakItem.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		backpakItem.setRegistryName(backpack.getKey());
		itemsToRegister.add(backpakItem);
		return backpakItem;
	}

	private void addRecipe(Item backpackT1, Item backpackT2, ItemStack crafting, IForgeRegistry<IRecipe> registry) {
		if (registry != null) {
			registry.register(new ShapedOreRecipe(backpackT1.getRegistryName(), new ItemStack(backpackT1), "X#X", "VYV", "X#X", '#', Blocks.WOOL,
					'X', Items.STRING, 'V', crafting, 'Y', Blocks.CHEST).setRegistryName(backpackT1.getRegistryName()));
		} else {
			RecipeManagers.carpenterManager.addRecipe(200, FluidRegistry.getFluidStack("water", 1000), null, new ItemStack(backpackT2), "WXW", "WTW", "WWW", 'X', Items.DIAMOND, 'W',
					ModuleCore.getItems().craftingMaterial.getSilkWisp(), 'T', backpackT1);
		}
	}

}

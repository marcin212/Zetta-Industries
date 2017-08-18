package com.bymarcin.zettaindustries.mods.battery;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.battery.block.CharcoalBlock;

import com.bymarcin.zettaindustries.utils.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CharcoalBlockMod implements IMod{
	public static CharcoalBlock charcoalblock;
	public static Item itemCharcoalblock;
	ItemStack coal;
	ItemStack coalx9;

	@Override
	public void preInit() {
		charcoalblock = new CharcoalBlock();
		itemCharcoalblock = (new CharcoalBlock.Item(charcoalblock).setRegistryName(charcoalblock.getRegistryName()));
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(charcoalblock);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(itemCharcoalblock);
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		coal = new ItemStack(Items.COAL, 1,1);
		coalx9 = new ItemStack(Items.COAL, 9,1);
		event.getRegistry().register(RecipeUtils.createShapelessRecipe(new ItemStack(charcoalblock),
				coal,coal,coal,
				coal,coal,coal,
				coal,coal,coal).setRegistryName(new ResourceLocation("zettaindustries:charcoal_block_from_coal")));
		event.getRegistry().register(RecipeUtils.createShapelessRecipe(coalx9, charcoalblock)
				.setRegistryName(new ResourceLocation("zettaindustries:coal_from_charcoal_block")));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		ZettaIndustries.proxy.registermodel(itemCharcoalblock,0);
	}

	@Override
	public void init() {
		OreDictionary.registerOre("blockCharcoal", charcoalblock);
	}

	@Override
	public void postInit() {

	}

}

package com.bymarcin.zettaindustries.mods.vanillautils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.vanillautils.block.VariableRedstoneEmitter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;


public class VanillaUtils implements IMod{
	public static VariableRedstoneEmitter variableredstoneemitter;
	public static Item variableredstoneemitteritem;

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
			ZettaIndustries.logger.info("create: variableredstoneemitter");
			variableredstoneemitter = new VariableRedstoneEmitter();
			variableredstoneemitteritem = new ItemBlock(variableredstoneemitter).setRegistryName(variableredstoneemitter.getRegistryName());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		if (variableredstoneemitteritem != null) {
			ZettaIndustries.proxy.registermodel(variableredstoneemitteritem, 0);
		}
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> register) {
		if (variableredstoneemitter != null)
			register.getRegistry().register(variableredstoneemitter);
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> register) {
		if (variableredstoneemitteritem != null)
			register.getRegistry().register(new ShapedOreRecipe(variableredstoneemitter.getRegistryName(), new ItemStack(variableredstoneemitteritem), "   ", "rzr", " x ",
					'x', Items.REPEATER, 'r', Items.REDSTONE, 'z', Blocks.REDSTONE_TORCH).setRegistryName(variableredstoneemitter.getRegistryName()));
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> register) {
		if (variableredstoneemitteritem != null)
			register.getRegistry().register(variableredstoneemitteritem);
	}
}

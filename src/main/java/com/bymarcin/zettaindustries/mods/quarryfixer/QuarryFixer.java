package com.bymarcin.zettaindustries.mods.quarryfixer;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.utils.RecipeUtils;

public class QuarryFixer implements IMod {
	static QuarryFixerBlock quarryfixerblock;
	static ItemBlock quarryfixeritem = new ItemBlock(quarryfixerblock);
	
	@Override
	public void init() {
		
		
	}
	
	@Override
	public void postInit() {
	
	}
	
	@Override
	public void preInit() {
		quarryfixerblock = new QuarryFixerBlock();
		quarryfixeritem = new ItemBlock(quarryfixerblock);
		quarryfixeritem.setCreativeTab(ZettaIndustries.tabZettaIndustries).setRegistryName(quarryfixerblock.getRegistryName());
	}
	
	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(quarryfixerblock);
	}
	
	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(quarryfixeritem);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRegisterModels(ModelRegistryEvent event) {
		ZettaIndustries.proxy.registermodel(quarryfixeritem, 0);
	}
	
	@SubscribeEvent
	public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(quarryfixeritem), " x ", "xdx", " x ",
				'x', "gearIron", 'd', Blocks.DISPENSER).setRegistryName(quarryfixeritem.getRegistryName()));
	}
	
	
}

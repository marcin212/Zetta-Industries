package com.bymarcin.zettaindustries.mods.ecatalogue;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.mail.ModuleMail;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.utils.RecipeUtils;
import li.cil.oc.api.Driver;
import li.cil.oc.api.Items;


public class ECatalogueMod implements IMod {
	public static ECatalogueBlock ecatalogueBlock;
	public static Item ecatalogueItem;
	public static MailmanItem mailmanItem;

	@Override
	public void preInit() {
		ecatalogueBlock = new ECatalogueBlock();
		mailmanItem = new MailmanItem();

		ecatalogueItem = (new ItemBlock(ecatalogueBlock).setRegistryName(ecatalogueBlock.getRegistryName()).setUnlocalizedName(ecatalogueBlock.getRegistryName().toString()));
		GameRegistry.registerTileEntity(ECatalogueTileEntity.class, "ecataloguetileentity");
	}

	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(ecatalogueBlock);
	}

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ecatalogueItem, mailmanItem);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRegisterModels(ModelRegistryEvent event) {
		ZettaIndustries.proxy.registermodel(mailmanItem, 0);
		ZettaIndustries.proxy.registermodel(ecatalogueItem, 0);
	}

	@SubscribeEvent
	public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
		ItemStack cpu1 = Items.get("cpu1").createItemStack(1);
		ItemStack microChip1 = Items.get("chip1").createItemStack(1);
		ItemStack stamp = ModuleMail.getItems().stamps.getItemStack();
		ItemStack catalogue = ModuleMail.getItems().catalogue.getItemStack(1);
		ItemStack iron = new ItemStack(net.minecraft.init.Items.IRON_INGOT, 1);
		ItemStack book = new ItemStack(net.minecraft.init.Items.BOOK, 1);
		event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(ecatalogueBlock), "IBI", "BCB", "IBI", 'I', iron, 'C', cpu1, 'B', book)
				.setRegistryName(ecatalogueBlock.getRegistryName()));
		event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(mailmanItem), "BSB", "MCM", "BSB", 'B', "ingotBronze", 'S', stamp, 'M', microChip1, 'C', catalogue)
				.setRegistryName(mailmanItem.getRegistryName()));
	}

	@Override
	public void init() {
		Driver.add(new TradeStationConventer());
		Driver.add((li.cil.oc.api.driver.DriverItem) mailmanItem);
	}

	@Override
	public void postInit() {
	}

}

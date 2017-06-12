package com.bymarcin.zettaindustries.mods.ecatalogue;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;

import li.cil.oc.api.Driver;
import li.cil.oc.api.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import forestry.mail.PluginMail;

public class ECatalogueMod implements IMod {
	public static final ECatalogueBlock ecatalogueBlock = new ECatalogueBlock();
	public static MailmanItem mailmanItem = new MailmanItem();

	@Override
	public void preInit() {
		GameRegistry.register(ecatalogueBlock);
		Item ecatalogueItem = GameRegistry.register(new ItemBlock(ecatalogueBlock).setRegistryName(ecatalogueBlock.getRegistryName()).setUnlocalizedName(ecatalogueBlock.getRegistryName().toString()));
		GameRegistry.registerTileEntity(ECatalogueTileEntity.class, "ecataloguetileentity");
		GameRegistry.register(mailmanItem);
		ZettaIndustries.proxy.registermodel(mailmanItem, 0);
		ZettaIndustries.proxy.registermodel(ecatalogueItem, 0);
	}

	@Override
	public void init() {
		Driver.add(new TradeStationConventer());
		Driver.add((li.cil.oc.api.driver.Item) mailmanItem);
	}

	@Override
	public void postInit() {
		ItemStack cpu1 = Items.get("cpu1").createItemStack(1);
		ItemStack microChip1 = Items.get("chip1").createItemStack(1);
		ItemStack stamp = PluginMail.items.stamps.getItemStack();
		ItemStack catalogue = PluginMail.items.catalogue.getItemStack(1);
		ItemStack iron = new ItemStack(net.minecraft.init.Items.IRON_INGOT, 1);
		ItemStack book = new ItemStack(net.minecraft.init.Items.BOOK, 1);
		GameRegistry.addRecipe(new ItemStack(ecatalogueBlock), "IBI", "BCB", "IBI", 'I', iron, 'C', cpu1, 'B', book);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(mailmanItem), "BSB", "MCM", "BSB", 'B', "ingotBronze", 'S', stamp, 'M', microChip1, 'C', catalogue));
	}

}

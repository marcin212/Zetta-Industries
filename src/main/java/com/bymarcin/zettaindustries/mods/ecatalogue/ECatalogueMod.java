package com.bymarcin.zettaindustries.mods.ecatalogue;

import com.bymarcin.zettaindustries.modmanager.IMod;

import li.cil.oc.api.Driver;
import li.cil.oc.api.Items;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.oredict.ShapedOreRecipe;

import forestry.plugins.PluginApiculture;
import forestry.plugins.PluginCore;
import forestry.plugins.PluginMail;


public class ECatalogueMod implements IMod{
	public static final ECatalogueBlock ecatalogueBlock = new ECatalogueBlock();
	public static MailmanItem mailmanItem = new MailmanItem();
	@Override
	public void preInit() {

	}
	
	@Override
	public void init() {
		GameRegistry.registerBlock(ecatalogueBlock, "ecatalogue");
		GameRegistry.registerTileEntity(ECatalogueTileEntity.class, "ecataloguetileentity");
		Driver.add(new TradeStationConventer());
		GameRegistry.registerItem(mailmanItem, "mailmanitem");
		Driver.add(mailmanItem);
	}

	@Override
	public void postInit() {
	    ItemStack cpu1= Items.get("cpu1").createItemStack(1);
	    ItemStack microChip1= Items.get("chip1").createItemStack(1);
	    ItemStack stamp = PluginMail.items.stamps.getItemStack();
	    ItemStack bronze = PluginCore.items.ingotBronze.getItemStack(1);
	    ItemStack catalogue = PluginMail.items.catalogue.getItemStack(1);
	    ItemStack iron= new ItemStack(net.minecraft.init.Items.iron_ingot,1);
		ItemStack book = new ItemStack(net.minecraft.init.Items.book,1);
		GameRegistry.addRecipe(new ItemStack(ecatalogueBlock), "IBI","BCB","IBI",'I', iron, 'C', cpu1, 'B', book);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(mailmanItem), "BSB","MCM","BSB",'B',"ingotBronze",'S',stamp,'M',microChip1,'C',catalogue));
		
	}

}

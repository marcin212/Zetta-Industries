package com.bymarcin.zettaindustries.mods.ecatalogue;

import com.bymarcin.zettaindustries.modmanager.IMod;

import li.cil.oc.api.Driver;
import li.cil.oc.api.Items;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class ECatalogueMod implements IMod{
	public static final ECatalogueBlock ecatalogueBlock = new ECatalogueBlock();
	
	@Override
	public void init() {
		GameRegistry.registerBlock(ecatalogueBlock, "ecatalogue");
		GameRegistry.registerTileEntity(ECatalogueTileEntity.class, "ecataloguetileentity");
		Driver.add(new TradeStationConventer());
	}

	@Override
	public void postInit() {
	    ItemStack cpu1= Items.get("cpu1").createItemStack(1);
	    ItemStack iron= new ItemStack(net.minecraft.init.Items.iron_ingot,1);
		ItemStack book = new ItemStack(net.minecraft.init.Items.book,1);
		GameRegistry.addRecipe(new ItemStack(ecatalogueBlock), "IBI","BCB","IBI",'I', iron, 'C', cpu1, 'B', book)	;
	}

}

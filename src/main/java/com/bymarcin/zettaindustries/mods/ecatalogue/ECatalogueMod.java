package com.bymarcin.zettaindustries.mods.ecatalogue;

import com.bymarcin.zettaindustries.modmanager.IMod;

import li.cil.oc.api.Driver;

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

		
	}

}

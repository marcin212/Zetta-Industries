package com.bymarcin.zettaindustries.mods.bundledviewer;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.rfpowermeter.RFMeterTileEntity;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BundledViewerMod implements IMod, IProxy{

	@Override
	public void preInit() {
		ZIRegistry.registerProxy(this);
		GameRegistry.registerBlock(new BundledViewerBlock(), "BundledViewer");
		GameRegistry.registerTileEntity(BundledViewerTileEntity.class, "BundledViewerTileEntity");
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		ClientRegistry.bindTileEntitySpecialRenderer(BundledViewerTileEntity.class, new BundledViewerRenderer());
		
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub
		
	}

}

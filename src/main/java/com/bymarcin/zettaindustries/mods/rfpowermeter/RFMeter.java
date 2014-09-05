package com.bymarcin.zettaindustries.mods.rfpowermeter;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.rfpowermeter.render.RFMeterRender;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class RFMeter implements IMod, IProxy{
	RFMeterBlock  meter;
	@Override
	public void init() {
		meter = new RFMeterBlock();
		GameRegistry.registerBlock(meter, "rfmeterblock");
		GameRegistry.registerTileEntity(RFMeterTileEntity.class, "rfmeterblock");
		ZIRegistry.registerProxy(this);
		ZIRegistry.registerPacket(4, RFMeterUpdatePacket.class, Side.CLIENT);
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientSide() {
		RFMeterRender render = new RFMeterRender();
		ClientRegistry.bindTileEntitySpecialRenderer(RFMeterTileEntity.class, render);
		MinecraftForgeClient.registerItemRenderer(new ItemStack(meter).getItem(), render);
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub
		
	}

}

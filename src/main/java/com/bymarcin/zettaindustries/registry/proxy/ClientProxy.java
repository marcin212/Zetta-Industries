package com.bymarcin.zettaindustries.registry.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends Proxy {
	
	@Override
	public void registermodel(Item item, int meta){
		registermodel(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	@Override
	public void registermodel(Item item, int meta, ModelResourceLocation modelResourceLocation){
		ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
	}
	
	@Override
	public void init() {
		for (IProxy p : proxy)
			p.clientSide();
	}
	
	public void loadComplete(){
		for (IProxy p : proxy){
			if(p instanceof INeedLoadCompleteEvent){
				((INeedLoadCompleteEvent)p).clientLoadComplete();
			}
		}
	}


	@Override
	public World getWorld(int dimensionId) {
		if (getCurrentClientDimension() != dimensionId) {
			return null;
		} else
			return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public int getCurrentClientDimension() {
		return Minecraft.getMinecraft().theWorld.provider.getDimension();
	}
}

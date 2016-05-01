package com.bymarcin.zettaindustries.registry.proxy;

import net.minecraft.item.Item;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.bymarcin.zettaindustries.registry.ZIRegistry;

public class Proxy extends ZIRegistry {

	public void registermodel(Item item, int meta){

	}
	
	public void init() {
		for (IProxy p : proxy)
			p.serverSide();
	}
	
	public void loadComplete(){
		for (IProxy p : proxy){
			if(p instanceof INeedLoadCompleteEvent){
				((INeedLoadCompleteEvent)p).serverLoadComplete();
			}
		}
	}

	public World getWorld(int dimensionId) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimensionId);
	}

	public int getCurrentClientDimension() {
		return -9001;
	}
}

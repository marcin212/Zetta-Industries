package com.bymarcin.zettaindustries.registry;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.registry.gui.GuiHandler;
import com.bymarcin.zettaindustries.registry.gui.IGUI;
import com.bymarcin.zettaindustries.registry.network.Packet;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;
import com.google.common.base.Preconditions;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class ZIRegistry {
	protected static Set<IProxy> proxy = new HashSet<IProxy>();
	protected static Set<IGUI> gui = new HashSet<IGUI>();
	private static Set<RPacket> packetsID = new HashSet<RPacket>();
	public static SimpleNetworkWrapper packetHandler;
	
	public static void preInitialize(){
		packetHandler = new SimpleNetworkWrapper(ZettaIndustries.MODID);
	}
	
	public static void initialize(){
		NetworkRegistry.INSTANCE.registerGuiHandler(ZettaIndustries.instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	}
//	public static <T extends IMessage & IMessageHandler<T, U>, U extends IMessage> void registerPacket(int id, Class<T> clazz, Side handleOn){
	
	public static <T extends Packet<T,U>, U extends IMessage> void registerPacket(int id, Class<T> clazz, Side handleOn){
		if(!packetsID.contains(new RPacket(id, handleOn))){
			packetsID.add(new RPacket(id, handleOn));
			packetHandler.registerMessage(clazz, clazz, id, handleOn);
		}else{
			ZettaIndustries.logger.error("Packet ID:"+ id +"try overwritting other packet!");
		}
	}
	
	public static void registerBucket(Block fluid, Item fluidBucket){
		BucketHandler.INSTANCE.buckets.put(fluid, fluidBucket);
	}
	
	public static void registerProxy(IProxy proxy){
		ZIRegistry.proxy.add(Preconditions.checkNotNull(proxy));
	}
	
	public static void registerGUI(IGUI gui){
		ZIRegistry.gui.add(gui);
	}
	
	private static class RPacket{
		@SuppressWarnings("unused")
		final int id;
		@SuppressWarnings("unused")
		final Side side;
		public RPacket(int id, Side side) {
			this.side = side;
			this.id = id;
		}
	}
	
}

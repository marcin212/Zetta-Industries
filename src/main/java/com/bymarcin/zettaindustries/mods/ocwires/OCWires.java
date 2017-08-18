package com.bymarcin.zettaindustries.mods.ocwires;

import blusunrize.immersiveengineering.client.models.smart.ConnLoader;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.ocwires.block.BlockTelecomunicationConnector;
import com.bymarcin.zettaindustries.mods.ocwires.item.ItemTelecommunicationWire;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.INeedLoadCompleteEvent;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import blusunrize.immersiveengineering.api.ManualHelper;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;

public class OCWires implements IMod, IProxy, INeedLoadCompleteEvent{
	public static BlockTelecomunicationConnector connector = new BlockTelecomunicationConnector();
	public static ItemTelecommunicationWire wire = new ItemTelecommunicationWire();
	public static int cableLength = 32;
	
	public OCWires() {
	
	}
	
	@Override
	public void preInit() {
		connector.setRegistryName(new ResourceLocation("zettaindustries:telecommunicationconnector"));

		GameRegistry.registerTileEntity(TileEntityTelecomunicationConnector.class, "TileEntityTelecomunicationConnector");
		ZIRegistry.registerProxy(this);
		MinecraftForge.EVENT_BUS.register(this);
		cableLength = ZettaIndustries.instance.config.get("OCWires", "cableLength", 32, "The maximum length cables can have.",8,64).getInt(32);	
	}

	@SubscribeEvent
	public void onRegisterBlock(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(connector);
	}

	@SubscribeEvent
	public void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(wire);
	}

	@SubscribeEvent
	public void onRegisterRecipe(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(new ShapedOreRecipe(connector.getRegistryName(), new ItemStack(connector,8), "BIB"," I ","BIB", 'I',li.cil.oc.api.Items.get("cable").createItemStack(1),'B',Blocks.HARDENED_CLAY).setRegistryName(connector.getRegistryName()));
		event.getRegistry().register(new ShapedOreRecipe(wire.getRegistryName(), new ItemStack(wire,4), " I ","ISI"," I ", 'I',li.cil.oc.api.Items.get("cable").createItemStack(1), 'S',"stickWood").setRegistryName(wire.getRegistryName()));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		ZettaIndustries.proxy.registermodel(wire, 0);
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {
		TelecommunicationWireType.register();
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void renderOverlay(RenderGameOverlayEvent.Post event)
	{
//		if(ClientUtils.mc().thePlayer!=null && ClientUtils.mc().thePlayer.getCurrentEquippedItem()!=null && event.type == RenderGameOverlayEvent.ElementType.TEXT)
//		{
//			if( OreDictionary.itemMatches(new ItemStack(wire,1,OreDictionary.WILDCARD_VALUE), ClientUtils.mc().thePlayer.getCurrentEquippedItem(), false) )
//			{
//				if(ItemNBTHelper.hasKey(ClientUtils.mc().thePlayer.getCurrentEquippedItem(), "linkingPos"))
//				{
//					int[] link = ItemNBTHelper.getIntArray(ClientUtils.mc().thePlayer.getCurrentEquippedItem(), "linkingPos");
//					if(link!=null&&link.length>3)
//					{
//						String s = StatCollector.translateToLocalFormatted(Lib.DESC_INFO+"attachedTo", link[1],link[2],link[3]);
//						ClientUtils.font().drawString(s, event.resolution.getScaledWidth()/2 - ClientUtils.font().getStringWidth(s)/2, event.resolution.getScaledHeight()-GuiIngameForge.left_height-10, 0xeda045, true);
//					}
//				}
//
//			}
//		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		ConnLoader.baseModels.put("conn_tele", new ResourceLocation("immersiveengineering:block/connector/connectorLV.obj"));
		ConnLoader.textureReplacements.put("conn_tele", ImmutableMap.of("#immersiveengineering:blocks/connector_connectorLV",
				ZettaIndustries.MODID+":blocks/ocwires/metal_connectorTC"));
		Item blockItem = Item.getItemFromBlock(connector);
		ZettaIndustries.proxy.registermodel(blockItem, 0);
	}
	
	
	@SideOnly(Side.CLIENT)
	public void manualPages(){
		ManualInstance manual = ManualHelper.getManual();
		manual.addEntry("ocwires", ZettaIndustries.MODID, 
				new ManualPages.Crafting(manual, "ocwires1", new ItemStack(OCWires.wire)),
				new ManualPages.Crafting(manual, "ocwires0", new ItemStack(OCWires.connector))	
		);
	}

	@Override
	public void serverSide() {
	
	}

	@Override
	public void serverLoadComplete() {
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientLoadComplete() {
		manualPages();
	}

}

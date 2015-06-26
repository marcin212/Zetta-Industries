package com.bymarcin.zettaindustries.mods.ocwires;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.ocwires.block.BlockTelecomunicationConnector;
import com.bymarcin.zettaindustries.mods.ocwires.item.ItemBlockTelecommunicationConnector;
import com.bymarcin.zettaindustries.mods.ocwires.item.ItemTelecommunicationWire;
import com.bymarcin.zettaindustries.mods.ocwires.render.BlockRenderTelecommunication;
import com.bymarcin.zettaindustries.mods.ocwires.render.RenderTelecommunicationConnector;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import blusunrize.immersiveengineering.api.WireType;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Lib;

public class OCWires implements IMod, IProxy{
	public static BlockTelecomunicationConnector connector = new BlockTelecomunicationConnector();
	public static ItemTelecommunicationWire wire = new ItemTelecommunicationWire();
	public static ItemBlockTelecommunicationConnector connectorItemBlock = new ItemBlockTelecommunicationConnector(connector);
	public OCWires() {
	
	}
	
	@Override
	public void preInit() {
		GameRegistry.registerBlock(connector, ItemBlockTelecommunicationConnector.class, "BlockTelecomunicationConnector");
	}
	
	@Override
	public void init() {
		TelecommunicationWireType.register();
		GameRegistry.registerItem(wire, "TelecommunicationWire");
		GameRegistry.registerTileEntity(TileEntityTelecomunicationConnector.class, "TileEntityTelecomunicationConnector");
		ZIRegistry.registerProxy(this);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@Override
	public void postInit() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(connector), "BIB"," I ","BIB", 'I',li.cil.oc.api.Items.get("cable").createItemStack(1),'B',Blocks.hardened_clay));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wire), " I ","ISI"," I ", 'I',li.cil.oc.api.Items.get("cable").createItemStack(1), 'S',"stickWood"));
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void renderOverlay(RenderGameOverlayEvent.Post event)
	{
		if(ClientUtils.mc().thePlayer!=null && ClientUtils.mc().thePlayer.getCurrentEquippedItem()!=null && event.type == RenderGameOverlayEvent.ElementType.TEXT)
		{
			if( OreDictionary.itemMatches(new ItemStack(wire,1,OreDictionary.WILDCARD_VALUE), ClientUtils.mc().thePlayer.getCurrentEquippedItem(), false) )
			{
				if(ItemNBTHelper.hasKey(ClientUtils.mc().thePlayer.getCurrentEquippedItem(), "linkingPos"))
				{
					int[] link = ItemNBTHelper.getIntArray(ClientUtils.mc().thePlayer.getCurrentEquippedItem(), "linkingPos");
					if(link!=null&&link.length>3)
					{
						String s = StatCollector.translateToLocalFormatted(Lib.DESC_INFO+"attachedTo", link[1],link[2],link[3]);
						ClientUtils.font().drawString(s, event.resolution.getScaledWidth()/2 - ClientUtils.font().getStringWidth(s)/2, event.resolution.getScaledHeight()-GuiIngameForge.left_height-10, WireType.ELECTRUM.getColour(), true);
					}
				}

			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTelecomunicationConnector.class, new RenderTelecommunicationConnector());
		RenderingRegistry.registerBlockHandler(new BlockRenderTelecommunication());
//		BlockTelecomunicationConnector.setRenderId(RenderingRegistry.getNextAvailableRenderId());
//		RenderTelecommunicationConnector connectorRender = new RenderTelecommunicationConnector();
//		RenderingRegistry.registerBlockHandler(connector.getRenderType(), connectorRender);
		
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub
		
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void textureStich(TextureStitchEvent.Post event)
	{
			RenderTelecommunicationConnector.model = (WavefrontObject) AdvancedModelLoader.loadModel(RenderTelecommunicationConnector.rl);
			rebindUVsToIcon(RenderTelecommunicationConnector.model, connector.getIcon(0, 0));
	}

	void rebindUVsToIcon(WavefrontObject model, IIcon icon)
	{
		float minU = icon.getInterpolatedU(0);
		float sizeU = icon.getInterpolatedU(16) - minU;
		float minV = icon.getInterpolatedV(0);
		float sizeV = icon.getInterpolatedV(16) - minV;

		for(GroupObject groupObject : model.groupObjects)
			for(Face face : groupObject.faces)
				for (int i = 0; i < face.vertices.length; ++i)
				{
					TextureCoordinate textureCoordinate = face.textureCoordinates[i];
					face.textureCoordinates[i] = new TextureCoordinate(
							minU + sizeU * textureCoordinate.u,
							minV + sizeV * textureCoordinate.v
							);
				}
	}

}

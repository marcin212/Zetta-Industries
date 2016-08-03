package com.bymarcin.zettaindustries.mods.ocwires;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.battery.Battery;
import com.bymarcin.zettaindustries.mods.ocwires.block.BlockTelecomunicationConnector;
import com.bymarcin.zettaindustries.mods.ocwires.item.ItemBlockTelecommunicationConnector;
import com.bymarcin.zettaindustries.mods.ocwires.item.ItemTelecommunicationWire;
import com.bymarcin.zettaindustries.mods.ocwires.render.BlockRenderTelecommunication;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.INeedLoadCompleteEvent;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;

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
		GameRegistry.register(connector);
		GameRegistry.register(new ItemBlockTelecommunicationConnector(connector).setRegistryName(connector.getRegistryName()));
		GameRegistry.register(wire);
		ZettaIndustries.proxy.registermodel(wire,0);
		GameRegistry.registerTileEntity(TileEntityTelecomunicationConnector.class, "TileEntityTelecomunicationConnector");
		ZIRegistry.registerProxy(this);
		MinecraftForge.EVENT_BUS.register(this);
		cableLength = ZettaIndustries.instance.config.get("OCWires", "cableLength", 32, "The maximum length cables can have.",8,64).getInt(32);	
	}
	
	@Override
	public void init() {

	}

	@Override
	public void postInit() {
		TelecommunicationWireType.register();
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(connector,8), "BIB"," I ","BIB", 'I',li.cil.oc.api.Items.get("cable").createItemStack(1),'B',Blocks.HARDENED_CLAY));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wire,4), " I ","ISI"," I ", 'I',li.cil.oc.api.Items.get("cable").createItemStack(1), 'S',"stickWood"));
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
		//RenderingRegistry.registerBlockHandler(new BlockRenderTelecommunication());
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


//	@SideOnly(Side.CLIENT)
//	private void registerFluidModel(IFluidBlock fluidBlock) {
//		final Item item = Item.getItemFromBlock((Block) fluidBlock);
//		assert item != null;
//
//		ModelBakery.registerItemVariants(item);
//		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(ImmersiveEngineering.MODID + ":models/block/connectorLV");
//
//		ModelLoader.setCustomMeshDefinition(item, OCWires.MeshDefinitionFix.create(stack -> modelResourceLocation));
//
//		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
//			@Override
//			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
//				return modelResourceLocation;
//			}
//		});
//	}
//
//	@SideOnly(Side.CLIENT)
//	interface MeshDefinitionFix extends ItemMeshDefinition {
//		ModelResourceLocation getLocation(ItemStack stack);
//
//		// Helper method to easily create lambda instances of this class
//		static ItemMeshDefinition create(OCWires.MeshDefinitionFix lambda) {
//			return lambda;
//		}
//
//		default ModelResourceLocation getModelLocation(ItemStack stack) {
//			return getLocation(stack);
//		}
//	}




	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void textureStich(TextureStitchEvent.Post event)
	{
//			BlockRenderTelecommunication.model = (WavefrontObject) AdvancedModelLoader.loadModel(BlockRenderTelecommunication.rl);
//			rebindUVsToIcon(BlockRenderTelecommunication.model, connector.getIcon(0, 0));
	}

//	void rebindUVsToIcon(WavefrontObject model, IIcon icon)
//	{
//		float minU = icon.getInterpolatedU(0);
//		float sizeU = icon.getInterpolatedU(16) - minU;
//		float minV = icon.getInterpolatedV(0);
//		float sizeV = icon.getInterpolatedV(16) - minV;
//
//		for(GroupObject groupObject : model.groupObjects)
//			for(Face face : groupObject.faces)
//				for (int i = 0; i < face.vertices.length; ++i)
//				{
//					TextureCoordinate textureCoordinate = face.textureCoordinates[i];
//					face.textureCoordinates[i] = new TextureCoordinate(
//							minU + sizeU * textureCoordinate.u,
//							minV + sizeV * textureCoordinate.v
//							);
//				}
//	}

	@Override
	public void serverLoadComplete() {
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientLoadComplete() {
		manualPages();
	}

}

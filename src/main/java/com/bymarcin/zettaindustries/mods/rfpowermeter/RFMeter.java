package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.rfpowermeter.render.RFMeterModel;
import com.bymarcin.zettaindustries.mods.rfpowermeter.render.RFMeterRender;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import com.bymarcin.zettaindustries.utils.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class RFMeter implements IMod, IProxy{
	public static RFMeterBlock  meter;
	public static Item itemMeter;
	public static boolean isOCAllowed = false;
	public static ItemStack receptionCoil = null;
	public static ItemStack transmissionCoil = null;
	public static ItemStack rfmeter = null;

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(this);
		isOCAllowed = ZettaIndustries.instance.config.get("rfmeter", "oc.methods.allowed", true).getBoolean(true) && Loader.isModLoaded("OpenComputers");
		meter = new RFMeterBlock();
		itemMeter = new RFMeterItem(meter).setRegistryName(meter.getRegistryName());
		GameRegistry.registerTileEntity(RFMeterTileEntity.class, "terfmeterblock");
		GameRegistry.registerTileEntity(RFMeterTileEntityOC.class, "terfmeterblockoc");
		ZIRegistry.registerProxy(this);
		ZIRegistry.registerPacket(4, RFMeterUpdatePacket.class, Side.CLIENT);
	}

	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(meter);
	}

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(itemMeter);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRegisterModels(ModelRegistryEvent event) {
		ZettaIndustries.proxy.registermodel(itemMeter, 0);
	}

	@SubscribeEvent
	public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
		receptionCoil = null;
		transmissionCoil = null;
		rfmeter = null;

		if(rfmeter!=null && transmissionCoil !=null && receptionCoil!=null){
			receptionCoil.setItemDamage(1);
			transmissionCoil.setItemDamage(2);
			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(meter),"IRI","IYI","IXI", 'X', transmissionCoil, 'Y', rfmeter, 'I', "ingotIron",'R',receptionCoil).setRegistryName(meter.getRegistryName()));
		}else{
			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(meter),"IXI","IYI","IXI", 'X', Items.COMPARATOR, 'Y', Blocks.REDSTONE_BLOCK, 'I', "ingotIron").setRegistryName(meter.getRegistryName()));
		}
	}

	@Override
	public void init() {
//		
//		if(Loader.isModLoaded("ComputerCraft")){
//			RFMeterIntegrationComputerCraft.computercraftInit();
//		}
		
	}

	@Override
	public void postInit() {
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	@Mod.EventHandler
	public void texture(TextureStitchEvent e){
		Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(RFMeterModel.cTexture1);
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		ClientRegistry.bindTileEntitySpecialRenderer(RFMeterTileEntity.class, RFMeterRender.render);
		ClientRegistry.bindTileEntitySpecialRenderer(RFMeterTileEntityOC.class, RFMeterRender.render);
	}

	@Override
	public void serverSide() {

	}
	
}

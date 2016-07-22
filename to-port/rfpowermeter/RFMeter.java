package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.rfpowermeter.render.RFMeterStaticRender;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class RFMeter implements IMod, IProxy{
	public static RFMeterBlock  meter;
	public static boolean isOCAllowed = true;
//	public static int renderId = RenderingRegistry.getNextAvailableRenderId();
	public static ItemStack receptionCoil = null;
	public static ItemStack transmissionCoil = null;
	public static ItemStack rfmeter = null;
	
	@Override
	public void init() {
		ZettaIndustries.instance.config.get("rfmeter", "oc.methods.allowed", true).getBoolean(true);
		meter = new RFMeterBlock();
		GameRegistry.register(meter);
		
		Item itemMeter = new ItemBlock(meter).setRegistryName(meter.getRegistryName());
		GameRegistry.register(itemMeter);
		ZettaIndustries.proxy.registermodel(itemMeter, 0);
		
		
		GameRegistry.registerTileEntity(RFMeterTileEntity.class, "terfmeterblock");
		GameRegistry.registerTileEntity(RFMeterTileEntityOC.class, "terfmeterblockoc");
		
		
//		ZIRegistry.registerProxy(this);
//		ZIRegistry.registerPacket(4, RFMeterUpdatePacket.class, Side.CLIENT);
//		
//		if(Loader.isModLoaded("ComputerCraft")){
//			RFMeterIntegrationComputerCraft.computercraftInit();
//		}
		
	}

	@Override
	public void postInit() {
//		receptionCoil = GameRegistry.findItemStack("ThermalExpansion", "material", 1);
//		transmissionCoil = GameRegistry.findItemStack("ThermalExpansion", "material", 1);
//		rfmeter = GameRegistry.findItemStack("ThermalExpansion", "meter", 1);
//
//		if(rfmeter!=null && transmissionCoil !=null && receptionCoil!=null){
//			receptionCoil.setItemDamage(1);
//			transmissionCoil.setItemDamage(2);
//			GameRegistry.addRecipe(new ItemStack(meter),"IRI","IYI","IXI", 'X', transmissionCoil, 'Y', rfmeter, 'I', Items.iron_ingot,'R',receptionCoil);
//		}else{
//			GameRegistry.addRecipe(new ItemStack(meter),"IXI","IYI","IXI", 'X', Items.comparator, 'Y', Blocks.redstone_block, 'I', Items.iron_ingot);
//		}
//		GameRegistry.addShapelessRecipe(new ItemStack(meter),meter);
	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		
//		ClientRegistry.bindTileEntitySpecialRenderer(RFMeterTileEntity.class, RFMeterStaticRender.render);
//		ClientRegistry.bindTileEntitySpecialRenderer(RFMeterTileEntityOC.class, RFMeterStaticRender.render);
//		RenderingRegistry.registerBlockHandler(RFMeterStaticRender.getInstance());
		//MinecraftForgeClient.registerItemRenderer(new ItemStack(meter).getItem(), render);
	}

	@Override
	public void serverSide() {

	}
	
	@Override
	public void preInit() {

	}

}

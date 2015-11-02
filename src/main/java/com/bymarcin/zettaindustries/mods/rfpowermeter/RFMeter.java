package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.rfpowermeter.render.RFMeterStaticRender;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RFMeter implements IMod, IProxy{
	public static RFMeterBlock  meter;
	public static boolean isOCAllowed = true;
	public static int renderId = RenderingRegistry.getNextAvailableRenderId();
	public static ItemStack receptionCoil = null;
	public static ItemStack transmissionCoil = null;
	public static ItemStack rfmeter = null;
	
	@Override
	public void init() {
		ZettaIndustries.instance.config.get("rfmeter", "oc.methods.allowed", true).getBoolean(true);
		meter = new RFMeterBlock();
		GameRegistry.registerBlock(meter, RFMeterItem.class, "rfmeterblock");
		GameRegistry.registerTileEntity(RFMeterTileEntity.class, "rfmeterblock");
		GameRegistry.registerTileEntity(RFMeterTileEntityOC.class, "rfmeterblockoc");
		ZIRegistry.registerProxy(this);
		ZIRegistry.registerPacket(4, RFMeterUpdatePacket.class, Side.CLIENT);
		
		if(Loader.isModLoaded("ComputerCraft")){
			IntegrationComputerCraft.computercraftInit();
		}
		
	}

	@Override
	public void postInit() {
		receptionCoil = GameRegistry.findItemStack("ThermalExpansion", "material", 1);
		transmissionCoil = GameRegistry.findItemStack("ThermalExpansion", "material", 1);
		rfmeter = GameRegistry.findItemStack("ThermalExpansion", "meter", 1);

		if(rfmeter!=null && transmissionCoil !=null && receptionCoil!=null){
			receptionCoil.setItemDamage(1);
			transmissionCoil.setItemDamage(2);
			GameRegistry.addRecipe(new ItemStack(meter),"IRI","IYI","IXI", 'X', transmissionCoil, 'Y', rfmeter, 'I', Items.iron_ingot,'R',receptionCoil);
		}else{
			GameRegistry.addRecipe(new ItemStack(meter),"IXI","IYI","IXI", 'X', Items.comparator, 'Y', Blocks.redstone_block, 'I', Items.iron_ingot);
		}
		GameRegistry.addShapelessRecipe(new ItemStack(meter),meter);
	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		
		ClientRegistry.bindTileEntitySpecialRenderer(RFMeterTileEntity.class, RFMeterStaticRender.render);
		ClientRegistry.bindTileEntitySpecialRenderer(RFMeterTileEntityOC.class, RFMeterStaticRender.render);
		RenderingRegistry.registerBlockHandler(RFMeterStaticRender.getInstance());
		//MinecraftForgeClient.registerItemRenderer(new ItemStack(meter).getItem(), render);
	}

	@Override
	public void serverSide() {

	}
	
	@Override
	public void preInit() {

	}

}

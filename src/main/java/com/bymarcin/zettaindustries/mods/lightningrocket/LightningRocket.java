package com.bymarcin.zettaindustries.mods.lightningrocket;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.INeedLoadCompleteEvent;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.oredict.ShapelessOreRecipe;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;


public class LightningRocket implements IMod, IProxy, INeedLoadCompleteEvent{

	public static double rainchance = 0.1;
	public static double thunderchance = 0.2;
	public static ItemLightningFirework itemLightningFirework = new ItemLightningFirework();
	
	@Override
	public void preInit() {
		ZettaIndustries.instance.config.get("LightningRocket", "rainchance", 0.1,"Chance to spawn lighting bolt during rain.(1.0 == 100%)").getDouble(0.1);	
		ZettaIndustries.instance.config.get("LightningRocket", "thunderchance", 0.2, "Chance to spawn lighting bolt during thunder.(1.0 == 100%)").getDouble(0.2);	
	}

	@Override
	public void init() {
		GameRegistry.registerItem(itemLightningFirework, "lightningfirework");
		ZIRegistry.registerProxy(this);
	}

	@Override
	public void postInit() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(itemLightningFirework, Items.fireworks, WireType.COPPER.getWireCoil().getItem()));
	}
	
	@SideOnly(Side.CLIENT)
	public void manualPages(){
		ManualInstance manual = ManualHelper.getManual();
		manual.addEntry("lightningfirework", ZettaIndustries.MODID, 
				new ManualPages.Crafting(manual, "lightningfirework0", new ItemStack(LightningRocket.itemLightningFirework))
		);
	}

	@Override
	public void serverLoadComplete() {
		
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void clientLoadComplete() {
		manualPages();
	}

	@Override
	public void clientSide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub
		
	}
	
}

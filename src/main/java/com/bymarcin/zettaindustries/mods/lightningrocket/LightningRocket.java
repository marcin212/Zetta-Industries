package com.bymarcin.zettaindustries.mods.lightningrocket;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;

import net.minecraft.init.Items;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.oredict.ShapelessOreRecipe;

import blusunrize.immersiveengineering.api.energy.WireType;


public class LightningRocket implements IMod{

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
	}

	@Override
	public void postInit() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(itemLightningFirework, Items.fireworks, WireType.COPPER.getWireCoil().getItem()));
	}
	
	
}

package com.bymarcin.zettaindustries.mods.powermeter;

import li.cil.oc.api.Driver;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import buildcraft.BuildCraftTransport;
import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.transport.Pipe;
import buildcraft.transport.TransportProxyClient;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.registry.IProxy;
import com.bymarcin.zettaindustries.registry.ZIRegistry;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PowerMeter implements IMod, IProxy {
	public static Item powermeterblock;
	public static ItemIconProvider iconProvider = new ItemIconProvider();
	
	@Override
	public void init() {
		createPipe(PowerMeterPipe.class);
		MinecraftForge.EVENT_BUS.register(this);
		ZIRegistry.registerProxy(this);
		Driver.add(new PowerMeterDriver());
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("rawtypes")
	private void createPipe(Class<? extends Pipe> clazz) {
		powermeterblock = BuildCraftTransport.buildPipe(PowerMeterPipe.class, "Power Meter",CreativeTabBuildCraft.PIPES, Items.redstone, Items.iron_ingot);
		powermeterblock.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
	}

	@EventHandler
	@SideOnly(Side.CLIENT)
	public void textureHook(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 1) {
			iconProvider.registerIcons(event.map);
		}
	}

	@Override
	public void clientSide() {
		MinecraftForgeClient.registerItemRenderer(PowerMeter.powermeterblock, TransportProxyClient.pipeItemRenderer);
	}

	@Override
	public void serverSide() {
		
	}
}

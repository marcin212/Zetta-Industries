package com.bymarcin.zettaindustries.mods.mgc;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.mgc.block.ElectricalConnectorBlock;
import com.bymarcin.zettaindustries.mods.mgc.block.LampSocketBlock;
import com.bymarcin.zettaindustries.mods.mgc.gui.LampSocketContainer;
import com.bymarcin.zettaindustries.mods.mgc.gui.LampSocketGUI;
import com.bymarcin.zettaindustries.mods.mgc.item.LightBulbItem;
import com.bymarcin.zettaindustries.mods.mgc.render.ElectricalConnectorRenderer;
import com.bymarcin.zettaindustries.mods.mgc.render.LampSocketRenderer;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalConnectorTileEntity;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.gui.IGUI;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MGC implements IMod, IGUI, IProxy {
	LampSocketBlock lampSocketBlock;
	LightBulbItem lightBulbItem;
	ElectricalConnectorBlock connector;

	@Override
	public void preInit() {
		lampSocketBlock = new LampSocketBlock();
		lightBulbItem = new LightBulbItem();
		connector = new ElectricalConnectorBlock();
		ZIRegistry.registerProxy(this);

	}

	@Override
	public void init() {
		ZIRegistry.registerGUI(this);
	}

	@Override
	public void postInit() {

	}

	@Override
	public Object getServerGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof LampSocketTileEntity) {
			return new LampSocketContainer(player.inventory, (LampSocketTileEntity) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof LampSocketTileEntity) {
			return new LampSocketGUI(player.inventory, (LampSocketTileEntity) tileEntity);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		ElectricalConnectorRenderer electricalConnectorRenderer = new ElectricalConnectorRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(ElectricalConnectorTileEntity.class, electricalConnectorRenderer);
		RenderingRegistry.registerBlockHandler(electricalConnectorRenderer);
		RenderingRegistry.registerBlockHandler(new LampSocketRenderer());
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub

	}

}

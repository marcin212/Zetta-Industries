package com.bymarcin.zettaindustries.mods.mgc.block;

import com.bymarcin.zettaindustries.mods.mgc.render.ElectricalConnectorRenderer;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalConnectorTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

public class ElectricalConnectorBlock extends BlockContainer{

	public ElectricalConnectorBlock() {
		super(Material.iron);
		GameRegistry.registerBlock(this, "ElectricalConnector");
		GameRegistry.registerTileEntity(ElectricalConnectorTileEntity.class, "ElectricalConnectorTileEntity");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new ElectricalConnectorTileEntity();
	}
	
	@Override
	public int getRenderType() {
		return ElectricalConnectorRenderer.renderid;
	}

}

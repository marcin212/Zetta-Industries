package com.bymarcin.zettaindustries.mods.ocwires.render;

import com.bymarcin.zettaindustries.mods.ocwires.OCWires;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import blusunrize.immersiveengineering.client.ClientUtils;

public class BlockRenderTelecommunication implements ISimpleBlockRenderingHandler{
 
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glScalef(1.25f, 1.25f, 1.25f);
		Tessellator.instance.startDrawingQuads();
		ClientUtils.handleStaticTileRenderer(new TileEntityTelecomunicationConnector());
		Tessellator.instance.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		ClientUtils.handleStaticTileRenderer(world.getTileEntity(x, y, z));
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return OCWires.connector.getRenderType();
	}
	


}

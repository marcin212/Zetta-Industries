package com.bymarcin.zettaindustries.mods.ocwires.render;

import com.bymarcin.zettaindustries.mods.ocwires.OCWires;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;

public class BlockRenderTelecommunication implements ISimpleBlockRenderingHandler{
	
	public static ResourceLocation rl = new ResourceLocation(ImmersiveEngineering.MODID+":models/connectorLV.obj");
	public static WavefrontObject model = (WavefrontObject) AdvancedModelLoader.loadModel(rl);
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glScalef(1.25f, 1.25f, 1.25f);
		Tessellator.instance.startDrawingQuads();
		ClientUtils.renderStaticWavefrontModel(new TileEntityTelecomunicationConnector(), model, Tessellator.instance, new Matrix4(), new Matrix4(),0 , false);
		Tessellator.instance.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(!(te instanceof TileEntityTelecomunicationConnector)) return false;
		TileEntityTelecomunicationConnector connector = (TileEntityTelecomunicationConnector)world.getTileEntity(x, y, z);
		Matrix4 translationMatrix = new Matrix4().translate(connector.xCoord, connector.yCoord, connector.zCoord);
		Matrix4 rotationMatrix = new Matrix4();
		translationMatrix.translate(.5, .5, .5);
		switch(connector.getFacing())
		{
		case 0:
			break;
		case 1:
			rotationMatrix.rotate(Math.toRadians(180), 0,0,1);
			break;
		case 2:
			rotationMatrix.rotate(Math.toRadians(90), 1,0,0);
			break;
		case 3:
			rotationMatrix.rotate(Math.toRadians(-90), 1,0,0);
			break;
		case 4:
			rotationMatrix.rotate(Math.toRadians(-90), 0,0,1);
			break;
		case 5:
			rotationMatrix.rotate(Math.toRadians(90), 0,0,1);
			break;
		}
		ClientUtils.renderStaticWavefrontModel(connector, model, Tessellator.instance, translationMatrix, rotationMatrix,0, false);
		ClientUtils.renderAttachedConnections(connector);
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

package com.bymarcin.zettaindustries.mods.ocwires.render;

import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.render.TileRenderImmersiveConnectable;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;

public class RenderTelecommunicationConnector extends TileRenderImmersiveConnectable{

	public static ResourceLocation rl = new ResourceLocation(ImmersiveEngineering.MODID+":models/connectorLV.obj");
	public static WavefrontObject model = (WavefrontObject) AdvancedModelLoader.loadModel(rl);
	
	@Override
	public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
		TileEntityTelecomunicationConnector connector = (TileEntityTelecomunicationConnector)tile;
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
		
		ClientUtils.renderStaticWavefrontModel(tile, model, tes, translationMatrix, rotationMatrix, true, false);

	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		renderDynamic(tile, x, y, z, f);
	}

}

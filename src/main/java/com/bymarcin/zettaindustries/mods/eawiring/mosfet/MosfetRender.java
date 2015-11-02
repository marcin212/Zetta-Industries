package com.bymarcin.zettaindustries.mods.eawiring.mosfet;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class MosfetRender implements ISimpleBlockRenderingHandler{
	public static ResourceLocation rl = new ResourceLocation(ZettaIndustries.MODID + ":textures/models/Mosfet.obj");
	CustomModel[] models = new CustomModel[4];
	
	
	public MosfetRender() {
		float angle = 0;
		for (int i = 0; i < 4; i++) {
			models[i] = CustomModelFactory.rotateModel(angle, 0, 1, 0, CustomModelFactory.createModel(rl).dispose());
			angle -= 90;
		}
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int lb = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int lb_j = lb % 65536;
		int lb_k = lb / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lb_j / 1.0F, (float) lb_k / 1.0F);
		TileEntity te = world.getTileEntity(x, y, z);
		int face = 0;
		if(te instanceof TEMosfet){
			face = ((TEMosfet) te).face;
		}
		
		Tessellator.instance.addTranslation(x, y, z);
		
		models[face].draw(Tessellator.instance, block.getIcon(0, 0), world, x, y, z, renderer);
		Tessellator.instance.addTranslation(-x, -y, -(z));
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return Mosfet.renderid;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator.instance.startDrawingQuads();
		models[0].drawInventory(Tessellator.instance, block.getIcon(0, 0));
		Tessellator.instance.draw();

	}
}

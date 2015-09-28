package com.bymarcin.zettaindustries.mods.simpledhd.render;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.simpledhd.SimpleDHD;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRenderSimpleDHD implements ISimpleBlockRenderingHandler{
	public static ResourceLocation rl = new ResourceLocation(ZettaIndustries.MODID+":textures/models/DHD3.obj");
	CustomModel[] models = new CustomModel[4];
	
	public BlockRenderSimpleDHD() {
		float angle = 0;
		for(int i =0 ; i<4; i++){
			models[i] = CustomModelFactory.rotateModel(angle, 0, 1, 0, CustomModelFactory.createModel(rl).dispose());
			angle-=90;
		}
	}
	  
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator.instance.startDrawingQuads();
		models[0].draw(Tessellator.instance, block.getIcon(0, 0));
		Tessellator.instance.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator.instance.addTranslation(x, y, z);
		Tessellator.instance.setColorRGBA_F(1, 1, 1, 1);
		models[world.getBlockMetadata(x, y, z)].draw(Tessellator.instance, block.getIcon(0, 0));
        Tessellator.instance.addTranslation(-x, -y, -(z));
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return SimpleDHD.simpledhd.getRenderType();
	}

}

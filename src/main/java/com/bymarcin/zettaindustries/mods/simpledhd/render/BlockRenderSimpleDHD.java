package com.bymarcin.zettaindustries.mods.simpledhd.render;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.simpledhd.SimpleDHD;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class BlockRenderSimpleDHD implements ISimpleBlockRenderingHandler{
	public static ResourceLocation rl1 = new ResourceLocation(ZettaIndustries.MODID+":textures/models/DHD1.obj");
	public static ResourceLocation rl2 = new ResourceLocation(ZettaIndustries.MODID+":textures/models/DHD2.obj");
	public static ResourceLocation rl3 = new ResourceLocation(ZettaIndustries.MODID+":textures/models/DHD3.obj");
	public static ResourceLocation rl4 = new ResourceLocation(ZettaIndustries.MODID+":textures/models/DHD4.obj");
	
	public static ResourceLocation texture = new ResourceLocation(ZettaIndustries.MODID+":textures/blocks/simpledhd/DHD.png");

	public static WavefrontObject  model1 = (WavefrontObject)AdvancedModelLoader.loadModel(rl1);
	public static WavefrontObject  model2 = (WavefrontObject)AdvancedModelLoader.loadModel(rl2);
	public static WavefrontObject  model3 = (WavefrontObject)AdvancedModelLoader.loadModel(rl3);
	public static WavefrontObject  model4 = (WavefrontObject)AdvancedModelLoader.loadModel(rl4);
	
	public static void realoadModels(){
		  model1 = (WavefrontObject)AdvancedModelLoader.loadModel(rl1);
		  model2 = (WavefrontObject)AdvancedModelLoader.loadModel(rl2);
		  model3= (WavefrontObject)AdvancedModelLoader.loadModel(rl3);
		  model4 = (WavefrontObject)AdvancedModelLoader.loadModel(rl4);
		
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator.instance.startDrawingQuads();
		model4.tessellateAll(Tessellator.instance);
		Tessellator.instance.draw();
 
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator.instance.addTranslation(x, y, z);
		Tessellator.instance.setColorRGBA_F(1, 1, 1, 1);
			switch (world.getBlockMetadata(x, y, z)) {
				case 0:
					 model3.tessellateAll(Tessellator.instance);
					 break;
				case 1:
					 model2.tessellateAll(Tessellator.instance);
					 break;
				case 2:
					 model1.tessellateAll(Tessellator.instance);
					 break;
				case 3:
					model4.tessellateAll(Tessellator.instance);
					break;
				default:
					model3.tessellateAll(Tessellator.instance);
			}
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

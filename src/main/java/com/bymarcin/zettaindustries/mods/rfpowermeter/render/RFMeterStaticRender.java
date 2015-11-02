package com.bymarcin.zettaindustries.mods.rfpowermeter.render;

import com.bymarcin.zettaindustries.mods.rfpowermeter.RFMeter;
import com.bymarcin.zettaindustries.mods.rfpowermeter.RFMeterTileEntity;
import com.bymarcin.zettaindustries.mods.simpledhd.render.BlockRenderSimpleDHD;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;
import com.bymarcin.zettaindustries.utils.render.cmd.Normal;
import com.bymarcin.zettaindustries.utils.render.cmd.VertexUV;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RFMeterStaticRender implements ISimpleBlockRenderingHandler{
	CustomModel[][] models = new CustomModel[4][2];
	private static RFMeterStaticRender INSTANCE;
	public static RFMeterRender render = new RFMeterRender();
	
	public RFMeterStaticRender() {
		for(int i=0;i<4;i++){
			models[i][0] = createModel(i, true);
			models[i][1] = createModel(i, false);
		}
	}
	
	public static RFMeterStaticRender getInstance(){
		if(INSTANCE==null) INSTANCE = new RFMeterStaticRender();
		return INSTANCE;
	}
	
	public CustomModel getInventoryModel(){
		return models[0][1];
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator.instance.startDrawingQuads();
		RenderHelper.enableStandardItemLighting();
		models[0][1].drawInventory(Tessellator.instance, block.getIcon(0, 0));
		Tessellator.instance.draw();
		render.renderItem(null, null);
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator.instance.addTranslation(x, y, z);
		drawShape(((RFMeterTileEntity)world.getTileEntity(x, y, z)).getBlockMetadata(), block, Tessellator.instance, world.getBlock(x, y, z).getMixedBrightnessForBlock(world, x, y, z), ((RFMeterTileEntity)world.getTileEntity(x, y, z)).isInverted(),
				 world,  x,  y,  z, renderer);
		Tessellator.instance.addTranslation(-x, -y, -z);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return RFMeter.renderId;
	}

	public CustomModel createModel(int dir, boolean isInverted){
		CustomModel model = CustomModelFactory.createModel();
		
		model.addCommand(new Normal(0, 0, -1));
		model.addCommand(new VertexUV(2/16f, 0, 2/16f, 12/64f, 38/64f, 0));
		model.addCommand(new VertexUV(2/16f, 1, 2/16f, 12/64f, 22/64f, 1));
		model.addCommand(new VertexUV(3/16f, 1, 2/16f, 11/64f, 22/64f, 2));
		model.addCommand(new VertexUV(3/16f, 0, 2/16f, 11/64f, 38/64f, 3));

		//left line front
		model.addCommand(new Normal(0, 0, -1));
		model.addCommand(new VertexUV(13/16f, 0, 2/16f, 1/64f, 38/64f, 0));
		model.addCommand(new VertexUV(13/16f, 1, 2/16f, 1/64f, 22/64f, 1));
		model.addCommand(new VertexUV(14/16f, 1, 2/16f, 0/64f, 22/64f, 2));
		model.addCommand(new VertexUV(14/16f, 0, 2/16f, 0/64f, 38/64f, 3));

		//line top front 15 22  27 23
		model.addCommand(new Normal(0, 0, -1));
		model.addCommand(new VertexUV(3/16f, 15/16f, 2/16f, 11/64f, 23/64f, 0));
		model.addCommand(new VertexUV(3/16f, 	    1, 2/16f, 11/64f, 22/64f, 1));
		model.addCommand(new VertexUV(13/16f, 	1, 2/16f, 1/64f,  22/64f, 2));
		model.addCommand(new VertexUV(13/16f,15/16f, 2/16f, 1/64f,  23/64f, 3));
		
		//line bottom front
		model.addCommand(new Normal(0, 0, -1));
		model.addCommand(new VertexUV(3/16f,  0,     2/16f, 11/64f ,38/64f, 0));
		model.addCommand(new VertexUV(3/16f,  1/16f, 2/16f, 11/64f ,37/64f, 1));
		model.addCommand(new VertexUV(13/16f, 1/16f, 2/16f, 1/64f ,37/64f, 2));
		model.addCommand(new VertexUV(13/16f, 0,     2/16f, 1/64f ,38/64f, 3));		

		//right inside line front
		model.addCommand(new Normal(1, 0, 0));
		model.addCommand(new VertexUV(3/16f, 1/16f,  2/16f, 25/64f ,52/64f, 0));
		model.addCommand(new VertexUV(3/16f, 15/16f, 2/16f, 25/64f, 38/64f, 1));
		model.addCommand(new VertexUV(3/16f, 15/16f, 3/16f, 26/64f, 38/64f, 2));
		model.addCommand(new VertexUV(3/16f, 1/16f,  3/16f, 26/64f, 52/64f, 3));

		//left inside line front
		model.addCommand(new Normal(-1, 0, 0));
		model.addCommand(new VertexUV(13/16f, 1/16f,  2/16f, 24/64f, 52/64f, 0));
		model.addCommand(new VertexUV(13/16f, 1/16f,  3/16f, 24/64f, 52/64f, 1));
		model.addCommand(new VertexUV(13/16f, 15/16f, 3/16f, 25/64f, 38/64f, 2));
		model.addCommand(new VertexUV(13/16f, 15/16f, 2/16f, 25/64f, 38/64f, 3));

		//line inside top front
		model.addCommand(new Normal(0, -1, 0));
		model.addCommand(new VertexUV(3/16f,  15/16f, 2/16f, 36/64f, 38/64f, 0));
		model.addCommand(new VertexUV(13/16f, 15/16f, 2/16f, 26/64f, 38/64f, 1));
		model.addCommand(new VertexUV(13/16f, 15/16f, 3/16f, 26/64f, 39/64f, 2));
		model.addCommand(new VertexUV(3/16f,  15/16f, 3/16f, 36/64f, 39/64f, 3));

		//line inside bottom front
		model.addCommand(new Normal(0, 1, 0));
		model.addCommand(new VertexUV(3/16f,  1/16f, 2/16f, 36/64f, 39/64f, 0));
		model.addCommand(new VertexUV(3/16f,  1/16f, 3/16f, 36/64f, 40/64f, 1));
		model.addCommand(new VertexUV(13/16f, 1/16f, 3/16f, 26/64f, 40/64f, 2));
		model.addCommand(new VertexUV(13/16f, 1/16f, 2/16f, 26/64f, 39/64f, 3));

		//inside front 
		model.addCommand(new Normal(0, 0, -1));
		model.addCommand(new VertexUV(3/16f,  1/16f,  3/16f, 11/64f,  37/64f, 0));
		model.addCommand(new VertexUV(3/16f,  15/16f, 3/16f, 11/64f,  23/64f , 1));
		model.addCommand(new VertexUV(13/16f, 15/16f, 3/16f, 1/64f, 23/64f , 2));
		model.addCommand(new VertexUV(13/16f, 1/16f,  3/16f, 1/64f, 37/64f, 3));
		
		//left
		model.addCommand(new Normal(1, 0, 0));
		model.addCommand(new VertexUV(14/16f, 0, 2/16f, 24/64f, 38/64f, 0));
		model.addCommand(new VertexUV(14/16f, 1, 2/16f, 24/64f, 22/64f, 1));
		model.addCommand(new VertexUV(14/16f, 1, 1,     38/64f, 22/64f, 2));
		model.addCommand(new VertexUV(14/16f, 0, 1,     38/64f, 38/64f, 3));

		//right
		model.addCommand(new Normal(-1, 0, 0));
		model.addCommand(new VertexUV(2/16f, 0, 2/16f, 38/64f, 38/64f, 0));
		model.addCommand(new VertexUV(2/16f, 0, 1,     52/64f, 38/64f, 1));
		model.addCommand(new VertexUV(2/16f, 1, 1,     52/64f, 22/64f, 2));
		model.addCommand(new VertexUV(2/16f, 1, 2/16f, 38/64f, 22/64f, 3));
		
		//back
		model.addCommand(new Normal(0, 0, 1));
		model.addCommand(new VertexUV(2/16f,  0, 1, 12/64f, 38/64f, 0));
		model.addCommand(new VertexUV(14/16f, 0, 1, 24/64f, 38/64f, 1));
		model.addCommand(new VertexUV(14/16f, 1, 1, 24/64f, 22/64f, 2));
		model.addCommand(new VertexUV(2/16f,  1, 1, 12/64f, 22/64f, 3));

		
		if(!isInverted){
			//top
			model.addCommand(new Normal(0, 1, 0));
			model.addCommand(new VertexUV(2/16f,  1, 1,     24/64f, 38/64f, 0));
			model.addCommand(new VertexUV(14/16f, 1, 1,     12/64f, 38/64f, 1));
			model.addCommand(new VertexUV(14/16f, 1, 2/16f, 12/64f, 52/64f, 2));
			model.addCommand(new VertexUV(2/16f,  1, 2/16f, 24/64f, 52/64f, 3));

			//bottom
			model.addCommand(new Normal(0, -1, 0));
			model.addCommand(new VertexUV(2/16f,  0, 2/16f, 12/64f,  38/64f, 0));
			model.addCommand(new VertexUV(14/16f, 0, 2/16f, 0/64f,  38/64f, 1));
			model.addCommand(new VertexUV(14/16f, 0, 1,     0/64f,  52/64f, 2));
			model.addCommand(new VertexUV(2/16f,  0, 1,     12/64f,  52/64f, 3));
		}else{
			//top
			model.addCommand(new Normal(0, 1, 0));
			model.addCommand(new VertexUV(2/16f,  1, 1,     12/64f,  52/64f, 0));
			model.addCommand(new VertexUV(14/16f, 1, 1,     0/64f,   52/64f, 1));
			model.addCommand(new VertexUV(14/16f, 1, 2/16f, 0/64f,   38/64f, 2));
			model.addCommand(new VertexUV(2/16f,  1, 2/16f, 12/64f,  38/64f, 3));

			//bottom
			model.addCommand(new Normal(0, -1, 0));
			model.addCommand(new VertexUV(2/16f,  0, 2/16f, 24/64f, 52/64f, 0));
			model.addCommand(new VertexUV(14/16f, 0, 2/16f, 12/64f, 52/64f, 1));
			model.addCommand(new VertexUV(14/16f, 0, 1,     12/64f, 38/64f, 2));
			model.addCommand(new VertexUV(2/16f,  0, 1,     24/64f, 38/64f, 3));
		}
		
		
		switch(dir){
			case 0: model = CustomModelFactory.rotateModel(180, 0, 1, 0, model.dispose()); break;
			case 1: model = CustomModelFactory.rotateModel(90, 0, 1, 0, model.dispose()); break;
			case 2: break;
			case 3: model = CustomModelFactory.rotateModel(270, 0, 1, 0, model.dispose());break;
		}
		return model;
	}
	
	
	
	private void drawShape(int w, Block block, Tessellator tes, int mixedBrightnessForBlock, boolean isInverted, IBlockAccess world, int x, int y, int z, RenderBlocks renderer){
		
		tes.setColorRGBA_F(.9F, .9F, .9F, 1);	
		//if(mixedBrightnessForBlock!=-1)
			//tes.setBrightness(mixedBrightnessForBlock);
		if(w>=0 && w<4){
			models[w][isInverted?0:1].draw(tes,block.getIcon(0, 0), world, x, y, z, renderer);
		}
	}
}

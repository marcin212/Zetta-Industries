package com.bymarcin.zettaindustries.mods.eawiring.connectors.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityConnectorBase;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorRHV;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;

public class EAConnectorRender implements ISimpleBlockRenderingHandler {
	public static final int renderid = RenderingRegistry.getNextAvailableRenderId();
	public static ResourceLocation[] rl = new ResourceLocation[6];
	CustomModel[][] models = new CustomModel[6][6];

	public EAConnectorRender() {
		rl[0] = new ResourceLocation(ZettaIndustries.MODID + ":textures/models/wireconnector.obj");
		rl[1] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/connectorLV.obj");
		rl[2] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/connectorMV.obj");
		rl[3] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/connectorHV.obj");
		rl[4] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/relayHV.obj");
		rl[5] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/connectorHV.obj");
		init();
	}
	
	private void init(){
		//public static final ForgeDirection[] VALID_DIRECTIONS = {DOWN, UP, NORTH, SOUTH, WEST, EAST};
		for (int j = 0; j < rl.length; j++) {
			float angle = 0;
			for (int i = 0; i < 6; i++) {
				if(j==0){
					models[j][i] = CustomModelFactory.rotateModel(angle, 0, 1, 0, CustomModelFactory.createModel(rl[j]).dispose());
				}else{
					int x =0;
					int y =0;
					int z =0;
					float a = angle;
					if(i==1){
						x=1;
						a = 180;
					}
					
					if(i==2){
						x=1;
						a=90;
					}
					
					if(i==3){
						x=1;
						a=-90;
					}
					
					if(i==4){
						z=1;
						a=-90;
					}
					
					if(i==5){
						z=1;
						a=90;
					}
					
					models[j][i] = CustomModelFactory.rotateModel(a, x, y, z, CustomModelFactory.translateModel(0.5f, 0.5f, 0.5f, CustomModelFactory.createModel(rl[j]).dispose()).dispose());
				}
				angle -= 90;
			}
		}
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator.instance.startDrawingQuads();
		models[metadata][0].drawInventory(Tessellator.instance, block.getIcon(0, metadata));
		Tessellator.instance.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		//init();
		int meta = world.getBlockMetadata(x, y, z);
		TileEntityConnectorBase te = (TileEntityConnectorBase) world.getTileEntity(x, y, z);
		int lb = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int lb_j = lb % 65536;
		int lb_k = lb / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lb_j / 1.0F, (float) lb_k / 1.0F);
		Tessellator tessellator = Tessellator.instance;
		
		tessellator.addTranslation(x, y, z);
		if(meta==0){
			models[meta][te.getQFacing()].draw(tessellator, block.getIcon(0, meta), world, x, y, z, renderer);
		} else {
			models[meta][te instanceof TileEntityEAConnectorRHV?ForgeDirection.getOrientation(te.getFacing()).getOpposite().ordinal():te.getFacing()].draw(tessellator, block.getIcon(0, meta), world, x, y, z, renderer);
		}
		tessellator.addTranslation(-(x), -(y), -(z));
		ClientUtils.renderAttachedConnections(world.getTileEntity(x, y, z));

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderid;
	}

}

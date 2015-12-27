package com.bymarcin.zettaindustries.mods.mgc.render;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class LampSocketRenderer implements ISimpleBlockRenderingHandler{
	public static int renderid = RenderingRegistry.getNextAvailableRenderId();
	ResourceLocation rl = new ResourceLocation(ZettaIndustries.MODID,"/textures/models/lampsocket.obj");
	CustomModel model = CustomModelFactory.createModel(rl);
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator.instance.addTranslation(x, y, z);
		model.draw(Tessellator.instance, block.getIcon(0, 0), world, x, y, z, renderer);
		Tessellator.instance.addTranslation(-x, -y, -z);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		return renderid;
	}

}

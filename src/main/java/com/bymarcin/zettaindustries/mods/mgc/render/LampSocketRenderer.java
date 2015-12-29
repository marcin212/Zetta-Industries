package com.bymarcin.zettaindustries.mods.mgc.render;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.block.LampSocketBlock;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;
import com.bymarcin.zettaindustries.utils.render.RenderUtils;
import com.bymarcin.zettaindustries.utils.render.RenderUtils.PartInfo;
import com.obj.Vertex;
import com.obj.WavefrontObject;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class LampSocketRenderer implements ISimpleBlockRenderingHandler {
	public static int renderid = RenderingRegistry.getNextAvailableRenderId();
	WavefrontObject[] models = new WavefrontObject[LampSocketBlock.subblocksCount];

	public LampSocketRenderer() {
		init();
	}

	public void init() {
			models[LampSocketBlock.basementsconce] = new WavefrontObject("/assets/zettaindustries/textures/blocks/mgc/models/basementsconce/basementsconce.obj");
			models[LampSocketBlock.chandelier] = new WavefrontObject("/assets/zettaindustries/textures/blocks/mgc/models/chandelier/chandelier.obj");
			models[LampSocketBlock.fluorescentlightsocket] = new WavefrontObject("/assets/zettaindustries/textures/blocks/mgc/models/fluorescentlightsocket/fluorescentlightsocket.obj");
			models[LampSocketBlock.sconce] = new WavefrontObject("/assets/zettaindustries/textures/blocks/mgc/models/sconce/sconce.obj");
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		PartInfo[] parts = ((LampSocketBlock) block).getRenderParts(null, block, 0, 0, 0, metadata);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addTranslation(0.0f, -0.6f, 0f);
		RenderUtils.drawModel(models[metadata], null, 0, 0, 0, block,0, parts);
		Tessellator.instance.addTranslation(0.0f, 0.6f, 0f);
		Tessellator.instance.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		init();
		int metadata = world.getBlockMetadata(x, y, z);
		int facing = ((LampSocketTileEntity)world.getTileEntity(x, y, z)).getFacing();
	PartInfo[] parts = ((LampSocketBlock) block).getRenderParts(world, block, x, y, z, metadata);
		if (parts != null) {
			RenderUtils.drawModel(models[metadata], world, x, y, z, block, facing, parts);
		}
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

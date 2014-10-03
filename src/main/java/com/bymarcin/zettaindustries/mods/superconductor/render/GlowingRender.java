package com.bymarcin.zettaindustries.mods.superconductor.render;

import org.lwjgl.opengl.GL11;

import com.bymarcin.zettaindustries.mods.superconductor.SuperConductorMod;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class GlowingRender implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		drawFaces(renderer, block, block.getIcon(0, 0), true);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (SuperConductorMod.pass == 0) {
			renderer.renderStandardBlock(block, x, y, z);
			return true;
		} else {
			renderGlow(world, x, y, z, block, renderer);
			return true;
		}
		//return false;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
	
	@Override
	public int getRenderId() {
		return SuperConductorMod.glowRenderID;
	}

	public static void drawFaces(RenderBlocks renderblocks, Block block, IIcon icon, boolean st) {
		drawFaces(renderblocks, block, icon, icon, icon, icon, icon, icon, st);
	}

	public static void drawFaces(RenderBlocks renderblocks, Block block, IIcon i1, IIcon i2, IIcon i3, IIcon i4, IIcon i5, IIcon i6, boolean solidtop) {
		Tessellator tessellator = Tessellator.instance;

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, i1);
		tessellator.draw();
		if (solidtop)
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, i2);
		tessellator.draw();
		if (solidtop)
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, i3);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, i4);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, i5);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, i6);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	protected static void renderGlow(IBlockAccess blockAccess, int x, int y, int z, Block block, RenderBlocks renderer) {

		Tessellator tessellator = Tessellator.instance;
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, 0);
		
		tessellator.setBrightness(0xF << 20 | 0xF << 4);
		tessellator.setColorOpaque_F(1F, 1F, 1F);

		IIcon tex = ((Glowing) block).getGlowIcon(blockAccess, x, y, z, 5);
		if (tex != null) {
			renderer.renderFaceXPos(block, x, y, z, tex);

		}
		tex = ((Glowing) block).getGlowIcon(blockAccess, x, y, z, 4);
		if (tex != null) {
			renderer.renderFaceXNeg(block, x, y, z, tex);

		}
		tex = ((Glowing) block).getGlowIcon(blockAccess, x, y, z, 3);
		if (tex != null) {
			renderer.renderFaceZPos(block, x, y, z, tex);

		}
		tex = ((Glowing) block).getGlowIcon(blockAccess, x, y, z, 2);
		if (tex != null) {
			renderer.renderFaceZNeg(block, x, y, z, tex);

		}
		tex = ((Glowing) block).getGlowIcon(blockAccess, x, y, z, 1);
		if (tex != null) {
			renderer.renderFaceYPos(block, x, y, z, tex);

		}
		tex = ((Glowing) block).getGlowIcon(blockAccess, x, y, z, 0);
		if (tex != null) {
			renderer.renderFaceYNeg(block, x, y, z, tex);
		}
	
	}
}

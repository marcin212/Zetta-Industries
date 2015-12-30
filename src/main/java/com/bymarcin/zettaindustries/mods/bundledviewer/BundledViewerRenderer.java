package com.bymarcin.zettaindustries.mods.bundledviewer;

import java.util.ArrayList;
import java.util.Collections;

import com.bymarcin.zettaindustries.ZettaIndustries;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.util.ForgeDirection;

public class BundledViewerRenderer extends TileEntitySpecialRenderer {
	final float angle90 = (float) Math.toRadians(90);
	final float angle180 = (float) Math.toRadians(180);
	final float angle270 = (float) Math.toRadians(-90);
	Matrix4f[] rotationMatrix = new Matrix4f[ForgeDirection.VALID_DIRECTIONS.length];
	Quad[] vertices;
	ResourceLocation rl = new ResourceLocation(ZettaIndustries.instance.MODID, "textures/blocks/empty.png");
	
	public BundledViewerRenderer() {
		init();
	}

	static class Quad {
		Vector4f[] vertices = new Vector4f[4];

		public void setVertices(int index, Vector4f vec) {
			vertices[index] = vec;
		}

		public void draw(Tessellator tes) {
			for (int i = 0; i < vertices.length; i++) {
				tes.addVertex(vertices[i].x, vertices[i].y, vertices[i].z);
			}
		}

		public Vector4f[] getVertices() {
			return vertices;
		}

	}

	public void init() {
		
		for (ForgeDirection fdir : ForgeDirection.VALID_DIRECTIONS) {
			Matrix4f m = new Matrix4f();
			m = m.translate(new Vector3f(.5f, .5f, .5f), m);
			switch (fdir) {
			case DOWN:
				m.rotate(angle180, new Vector3f(0, 1, 0));
				m.rotate(angle270, new Vector3f(1, 0, 0));
				break;
			case NORTH:
				break;
			case EAST:
				m.rotate(angle270, new Vector3f(0, 1, 0));
				break;
			case WEST:
				m.rotate(angle90, new Vector3f(0, 1, 0));
				break;
			case SOUTH:
				m.rotate(angle180, new Vector3f(0, 1, 0));
				break;
			case UNKNOWN:
				break;
			case UP:
				m.rotate(angle90, new Vector3f(0, 0, 1));
				m.rotate(angle90, new Vector3f(1, 0, 0));
				m.rotate(angle270, new Vector3f(0, 1, 0));
				break;
			default:
				break;
			}
			m = m.translate(new Vector3f(-.5f, -.5f, -.5f), m);
			rotationMatrix[fdir.ordinal()] = m;
		}
		
		
		///
		ArrayList<Quad> vertices = new ArrayList<Quad>();
		float offsetX = 1 / 32f;
		float offsetY = -4 / 16f + 1 / 32f;

		for (int i = 16; i > 0; i--) {
			if (i % 4 == 0) {
				offsetX = 1 / 32f;
				offsetY += 4 / 16f;
			}
			Quad q = new Quad();
			q.setVertices(0, new Vector4f(offsetX + 0, offsetY + 0, -0.001f, 1));
			q.setVertices(1, new Vector4f(offsetX + 0, offsetY + 3 / 16f, -0.001f, 1));
			q.setVertices(2, new Vector4f(offsetX + 3 / 16f, offsetY + 3 / 16f, -0.001f, 1));
			q.setVertices(3, new Vector4f(offsetX + 3 / 16f, offsetY + 0, -0.001f, 1));
			vertices.add(q);
			offsetX += 4 / 16f;
		}
		Collections.reverse(vertices);
		this.vertices = vertices.toArray(new Quad[0]);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float t) {
		
		byte[] signals = ((BundledViewerTileEntity) te).getSignals();
		bindTexture(rl);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addTranslation((float) x, (float) y, (float) z);
		Tessellator.instance.setBrightness(0xF << 20 | 0xF << 4);

		for (int i = 0; i < signals.length; i++) {
			if (signals[i] != 0) {
				Tessellator.instance.setColorRGBA_F(EntitySheep.fleeceColorTable[i][0] * 1.1f, EntitySheep.fleeceColorTable[i][1] * 1.1f, EntitySheep.fleeceColorTable[i][2] * 1.1f, 1);
			} else {
				Tessellator.instance.setColorRGBA_F(EntitySheep.fleeceColorTable[i][0] * 0.2f, EntitySheep.fleeceColorTable[i][1] * 0.2f, EntitySheep.fleeceColorTable[i][2] * 0.2f, 1);
			}

			for (int dir = 1; dir < 6; dir++) {
				Matrix4f m = rotationMatrix[dir];
				for (int k = 0; k < 4; k++) {
					Tessellator.instance.setNormal(0, 1,0);
					Vector4f result = Matrix4f.transform(m, new Vector4f(vertices[i].getVertices()[k].x, vertices[i].getVertices()[k].y, vertices[i].getVertices()[k].z, 1f), null);
					Tessellator.instance.addVertex(result.x, result.y, result.z);
				}
			}

		}

		Tessellator.instance.draw();
		Tessellator.instance.addTranslation((float) -(x), (float) -(y), (float) -z);
	}

}

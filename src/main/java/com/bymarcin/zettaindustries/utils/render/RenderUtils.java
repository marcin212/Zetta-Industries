package com.bymarcin.zettaindustries.utils.render;

import com.obj.Face;
import com.obj.Group;
import com.obj.TextureCoordinate;
import com.obj.Vertex;
import com.obj.WavefrontObject;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.common.util.ForgeDirection;

public class RenderUtils {
	public static final ForgeDirection[] FORGE_DIRECTIONS = { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST };

	public static float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	public static class PartInfo {
		String name;
		IIcon icon;

		public PartInfo(String name, IIcon icon) {
			this.name = name;
			this.icon = icon;
		}

		public IIcon getIcon() {
			return icon;
		}

		public String getName() {
			return name;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PartInfo) {
				return ((PartInfo) obj).name.equals(name);
			}
			return false;
		}
	}

	public static void drawModel(WavefrontObject model, IBlockAccess world, int x, int y, int z, Block block, int face, PartInfo... parts) {
		LightInfo[] currentLight = new LightInfo[6];
		if (world != null) {
			for (int i = 0; i < 6; i++) {
				currentLight[i] = LightInfo.calculateBlockLighting(i, world, block, x, y, z, 1, 1, 1);
			}

			int lb = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
			int lb_j = lb % 65536;
			int lb_k = lb / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lb_j / 1.0F, (float) lb_k / 1.0F);
		}
		Tessellator.instance.addTranslation(x, y, z);
		
		Matrix4f m = new Matrix4f();
		m.rotate(90, new Vector3f(model.rotate.getX(),model.rotate.getY(),model.rotate.getZ()));
		
		for (Group g : model.getGroups()) {
			PartInfo currentPart = null;
			for (PartInfo p : parts) {
				if (p.name.equals(g.getName())) {
					currentPart = p;
					break;
				}
			}
			if (currentPart == null)
				continue;

			int h = g.getMaterial().getTexture().getHeight();
			int w = g.getMaterial().getTexture().getWidth();

			for (Face f : g.getFaces()) {
				Vertex[] v = f.getVertices();
				Vertex[] n = f.getNormals();
				TextureCoordinate[] uv = f.getTextures();

				for (int i = 0; i < v.length; i++) {
					if (world != null) {
						float biggestNormal = Math.max(Math.abs(n[i].getY()), Math.max(Math.abs(n[i].getX()), Math.abs(n[i].getZ())));
						int side = biggestNormal == Math.abs(n[i].getY()) ? (n[i].getY() < 0 ? 0 : 1) : biggestNormal == Math.abs(n[i].getZ()) ? (n[i].getZ() < 0 ? 2 : 3) : (n[i].getX() < 0 ? 4 : 5);
						LightInfo faceLight = currentLight[side];
						int corner = (int) (i / (float) v.length * 4);
						Tessellator.instance.setBrightness(corner == 0 ? faceLight.brightnessTopLeft : corner == 1 ? faceLight.brightnessBottomLeft : corner == 2 ? faceLight.brightnessBottomRight : faceLight.brightnessTopRight);
						float r = corner == 0 ? faceLight.colorRedTopLeft : corner == 1 ? faceLight.colorRedBottomLeft : corner == 2 ? faceLight.colorRedBottomRight : faceLight.colorRedTopRight;
						float gr = corner == 0 ? faceLight.colorGreenTopLeft : corner == 1 ? faceLight.colorGreenBottomLeft : corner == 2 ? faceLight.colorGreenBottomRight : faceLight.colorGreenTopRight;
						float b = corner == 0 ? faceLight.colorBlueTopLeft : corner == 1 ? faceLight.colorBlueBottomLeft : corner == 2 ? faceLight.colorBlueBottomRight : faceLight.colorBlueTopRight;
						Tessellator.instance.setColorOpaque_F(r, gr, b);
					}
					Tessellator.instance.setNormal(n[i].getX(), n[i].getY(), n[i].getZ());
					Vector4f result = Matrix4f.transform(m, new Vector4f(v[i].getX(), v[i].getY(), v[i].getZ(),1f), null);
					Tessellator.instance.addVertexWithUV(
							result.x, result.y, result.z,
							RenderUtils.lerp(currentPart.getIcon().getMinU(), currentPart.getIcon().getMaxU(), uv[i].getU()),
							RenderUtils.lerp(currentPart.getIcon().getMinV(), currentPart.getIcon().getMaxV(), uv[i].getV())
							);
				}
			}
		}
		Tessellator.instance.addTranslation(-x, -y, -z);
	}

}

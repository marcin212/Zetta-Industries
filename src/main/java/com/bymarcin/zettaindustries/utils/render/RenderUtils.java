package com.bymarcin.zettaindustries.utils.render;

import com.bymarcin.zettaindustries.utils.LocalSides;
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
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.common.util.ForgeDirection;

public class RenderUtils {
	public static final ForgeDirection[] FORGE_DIRECTIONS = { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST };
	public static final float angle90 = (float) Math.toRadians(90);
	public static final float angle180 = (float) Math.toRadians(180);
	public static final float angle270 = (float) Math.toRadians(-90);
	public static Matrix4f[][] rotationMatrix;

	static {
		rotationMatrix = new Matrix4f[ForgeDirection.VALID_DIRECTIONS.length][LocalSides.values().length];
		for (ForgeDirection fdir : ForgeDirection.VALID_DIRECTIONS) {
			for (LocalSides ldir : LocalSides.values()) {
				Matrix4f m = new Matrix4f();
				m = m.translate(new Vector3f(.5f, .5f, .5f), m);
				switch (fdir.getOpposite()) {
				case DOWN:
					m.rotate(angle180, new Vector3f(1, 0, 0));
					break;
				case NORTH:
					m.rotate(angle270, new Vector3f(0, 0, 1));
					m.rotate(angle270, new Vector3f(1, 0, 0));
					break;
				case EAST:
					m.rotate(angle270, new Vector3f(0, 0, 1));
					break;
				case WEST:
					m.rotate(angle90, new Vector3f(0, 0, 1));
					break;
				case SOUTH:
					m.rotate(angle90, new Vector3f(0, 0, 1));
					m.rotate(angle90, new Vector3f(1, 0, 0));
					break;
				case UNKNOWN:
					break;
				case UP:
					break;
				default:
					break;
				}

				ldir.rotateMatrix(m);
				m = m.translate(new Vector3f(-.5f, -.5f, -.5f), m);
				rotationMatrix[fdir.ordinal()][ldir.ordinal()] = m;
			}
		}
	}

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

	public static void drawModel(WavefrontObject model, IBlockAccess world, int x, int y, int z, Block block, int face, LocalSides iface, PartInfo... parts) {
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

		
		Matrix4f m = new Matrix4f(rotationMatrix[face][iface.ordinal()]);

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

				Matrix4f matrix = new Matrix4f(m);
				matrix = Matrix4f.invert(matrix, matrix);
				matrix = matrix.transpose(matrix);

				for (int i = 0; i < v.length; i++) {

					Vector4f resultNormal = Matrix4f.transform(m, new Vector4f(n[i].getX(), n[i].getY(), n[i].getZ(), 0f), null);

					if (world != null) {
						float biggestNormal = Math.max(Math.abs(resultNormal.y), Math.max(Math.abs(resultNormal.x), Math.abs(resultNormal.z)));
						int side = biggestNormal == Math.abs(resultNormal.y) ? (resultNormal.y < 0 ? 0 : 1) : biggestNormal == Math.abs(resultNormal.z) ? (resultNormal.z < 0 ? 2 : 3) : (resultNormal.x < 0 ? 4 : 5);
						LightInfo faceLight = currentLight[side];
						int corner = (int) (i / (float) v.length * 4);
						Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
						float r = corner == 0 ? faceLight.colorRedTopLeft : corner == 1 ? faceLight.colorRedBottomLeft : corner == 2 ? faceLight.colorRedBottomRight : faceLight.colorRedTopRight;
						float gr = corner == 0 ? faceLight.colorGreenTopLeft : corner == 1 ? faceLight.colorGreenBottomLeft : corner == 2 ? faceLight.colorGreenBottomRight : faceLight.colorGreenTopRight;
						float b = corner == 0 ? faceLight.colorBlueTopLeft : corner == 1 ? faceLight.colorBlueBottomLeft : corner == 2 ? faceLight.colorBlueBottomRight : faceLight.colorBlueTopRight;
						Tessellator.instance.setColorOpaque_F(1f - (0.2f * r), 1f - (0.2f * gr), 1f - (0.2f * b));
					}

					Tessellator.instance.setNormal(resultNormal.x, resultNormal.y, resultNormal.z);
					Vector4f result = Matrix4f.transform(m, new Vector4f(v[i].getX(), v[i].getY(), v[i].getZ(), 1f), null);
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

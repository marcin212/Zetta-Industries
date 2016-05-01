package com.bymarcin.zettaindustries.utils.render.cmd.executor;

import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.LightInfo;

import net.minecraft.client.renderer.Tessellator;

public class VertexUVExecutor extends VertexExecutor {
	float u, v;

	public VertexUVExecutor(float x, float y, float z, float u, float v, int corner) {
		super(x, y, z, corner);
		this.u = u;
		this.v = v;
	}

	@Override
	public void execute(Tessellator tes, CustomModel model, boolean isInventory) {
//		if (!isInventory) {
//			LightInfo li = model.getCurrentLightForLastNormalDir();
//			switch (getCorner()) {
//			case 0:
//				tes.setColorOpaque_F(li.colorRedTopLeft, li.colorGreenTopLeft, li.colorBlueTopLeft);
//				tes.setBrightness(li.brightnessTopLeft);
//				break;
//			case 1:
//				tes.setColorOpaque_F(li.colorRedBottomLeft, li.colorGreenBottomLeft, li.colorBlueBottomLeft);
//				tes.setBrightness(li.brightnessBottomLeft);
//				break;
//			case 2:
//				tes.setColorOpaque_F(li.colorRedBottomRight, li.colorGreenBottomRight, li.colorBlueBottomRight);
//				tes.setBrightness(li.brightnessBottomRight);
//				break;
//			case 3:
//				tes.setColorOpaque_F(li.colorRedTopRight, li.colorGreenTopRight, li.colorBlueTopRight);
//				tes.setBrightness(li.brightnessTopRight);
//				break;
//			}
//		}
//		tes.addVertexWithUV(x, y, z, u, v);
	}

	@Override
	public String toString() {
		return String.format("[X: %f, Y: %f, Z: %f, U: %f, V: %f]", x, y, z, u, v);
	}
}

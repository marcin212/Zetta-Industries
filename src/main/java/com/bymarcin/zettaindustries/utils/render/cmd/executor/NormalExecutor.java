package com.bymarcin.zettaindustries.utils.render.cmd.executor;

import com.bymarcin.zettaindustries.utils.render.CustomModel;

import net.minecraft.client.renderer.Tessellator;

public class NormalExecutor implements IRenderCommandExecutor{
	float x, y, z;
	int mcDir;
	
	public NormalExecutor(float x, float y, float z, int mcDir) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.mcDir = mcDir;
	}
	
	@Override
	public void execute(Tessellator tes, CustomModel model, boolean isInventory) {
		model.setLastNormalDir(mcDir);
		tes.setNormal(x, y, z);
	}

	@Override
	public String toString() {
		return String.format("[X: %f, Y: %f, Z: %f]", x, y, z);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	
}

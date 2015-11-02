package com.bymarcin.zettaindustries.utils.render.cmd.executor;

import com.bymarcin.zettaindustries.utils.render.CustomModel;

import net.minecraft.client.renderer.Tessellator;

public class VertexExecutor implements IRenderCommandExecutor{
	float x, y, z;
	int corner;
	
	public VertexExecutor(float x, float y, float z, int corner) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.corner = corner;
	}
	
	@Override
	public void execute(Tessellator tes, CustomModel model, boolean isInventory) {
		tes.addVertex(x, y, z);
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
	
	public int getCorner() {
		return corner;
	}

}

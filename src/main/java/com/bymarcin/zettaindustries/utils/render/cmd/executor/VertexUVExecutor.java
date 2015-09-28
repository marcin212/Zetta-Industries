package com.bymarcin.zettaindustries.utils.render.cmd.executor;

import net.minecraft.client.renderer.Tessellator;

public class VertexUVExecutor extends VertexExecutor{
	float u, v;

	public VertexUVExecutor(float x, float y, float z, float u, float v) {
		super(x, y, z);
		this.u = u;
		this.v = v;
	}
	
	@Override
	public void execute(Tessellator tes) {
		tes.addVertexWithUV(x, y, z, u, v);
	}
	
	@Override
	public String toString() {
		return String.format("[X: %f, Y: %f, Z: %f, U: %f, V: %f]", x, y, z, u, v);
	}
}

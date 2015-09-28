package com.bymarcin.zettaindustries.utils.render.cmd.executor;

import net.minecraft.client.renderer.Tessellator;

public class NormalExecutor extends VertexExecutor{

	public NormalExecutor(float x, float y, float z) {
		super(x, y, z);
	}
	
	@Override
	public void execute(Tessellator tes) {
		tes.setNormal(x, y, z);
	}

	@Override
	public String toString() {
		return String.format("[X: %f, Y: %f, Z: %f]", x, y, z);
	}
	
}

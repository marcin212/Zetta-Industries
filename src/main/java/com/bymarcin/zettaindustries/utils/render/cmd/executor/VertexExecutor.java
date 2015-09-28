package com.bymarcin.zettaindustries.utils.render.cmd.executor;

import net.minecraft.client.renderer.Tessellator;

public class VertexExecutor implements IRenderCommandExecutor{
	float x, y, z;
	
	public VertexExecutor(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void execute(Tessellator tes) {
		tes.addVertex(x, y, z);
	}

}

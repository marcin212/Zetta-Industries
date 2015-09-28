package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.RenderUtils;
import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;
import com.bymarcin.zettaindustries.utils.render.cmd.executor.VertexUVExecutor;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class VertexUV extends RenderCommand {

	public VertexUV(float x, float y, float z, float u, float v) {
		super(VERTEXUV);
		args = new float[] { x, y, z, u, v };
	}

	@Override
	public IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV) {
		Vector4f n = Matrix4f.transform(transformations.getLast(), new Vector4f(args[0], args[1], args[2],1f), null);
		return new VertexUVExecutor(n.x/n.w , n.y/n.w, n.z/n.w, RenderUtils.lerp(minU, maxU, args[3]), RenderUtils.lerp(minV, maxV, args[4]));
	}
}

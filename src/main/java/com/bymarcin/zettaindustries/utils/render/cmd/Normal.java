package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;
import com.bymarcin.zettaindustries.utils.render.cmd.executor.NormalExecutor;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class Normal extends RenderCommand {

	public Normal(float x, float y, float z) {
		super(NORMAL);
		args = new float[] { x, y, z };
	}

	@Override
	public IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV) {
		Vector4f result = Matrix4f.transform(transformations.getLast(), new Vector4f(args[0], args[1], args[2],1f), null);
		return new NormalExecutor(result.x/result.w, result.y/result.w, result.z/result.w);
		//System.out.println(String.format("%f,%f,%f",args[0], args[1], args[2]));
		//return new NormalExecutor(args[0], args[1], args[2]);
	}
	
}

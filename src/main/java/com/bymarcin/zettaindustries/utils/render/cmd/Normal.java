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
		Matrix4f matrix = new Matrix4f(transformations.getLast());
		matrix = Matrix4f.invert(matrix,matrix);
		matrix = matrix.transpose(matrix);
		Vector4f result = Matrix4f.transform(matrix, new Vector4f(args[0], args[1], args[2],0f), null);
		float biggestNormal = Math.max(Math.abs(args[1]), Math.max(Math.abs(args[0]),Math.abs( args[2])));
		int side = biggestNormal==Math.abs(args[1])?(args[1]<0?0:1): biggestNormal==Math.abs( args[2])?( args[2]<0?2:3): (args[0]<0?4:5);	
		return new NormalExecutor(result.x, result.y, result.z, side);
	}
	
}

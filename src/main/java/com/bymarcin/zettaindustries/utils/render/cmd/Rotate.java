package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Rotate extends RenderCommand {

	public Rotate(float angle, float x, float y, float z) {
		super(ROTATE);
		args = new float[] { (float) Math.toRadians(angle), x, y, z };
	}

	@Override
	public IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV) {
		transformations.getLast().rotate(args[0], new Vector3f(args[1], args[2], args[3]));
		return null;
	}

}

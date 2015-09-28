package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Scale extends RenderCommand {

	public Scale(float x, float y, float z) {
		super(SCALE);
		args = new float[] { z, y, z };
	}

	@Override
	public IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV) {
		transformations.getLast().scale(new Vector3f(args[0],args[1],args[2]));
		return null;
	}
}

package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;

import org.lwjgl.util.vector.Matrix4f;

public class PushMatrix extends RenderCommand {

	public PushMatrix() {
		super(PUSHMATRIX);
	}

	@Override
	public IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV) {
		transformations.add(transformations.getLast());
		return null;
	}
}

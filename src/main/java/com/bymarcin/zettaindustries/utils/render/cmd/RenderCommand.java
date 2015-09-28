package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;

import org.lwjgl.util.vector.Matrix4f;

public abstract class RenderCommand {
	public static final int ROTATE = 0;
	public static final int TRANSLATE = 1;
	public static final int VERTEX = 2;
	public static final int COLOR = 3;
	public static final int SCALE = 4;
	public static final int PUSHMATRIX = 5;
	public static final int POPMATRIX = 6;
	public static final int VERTEXUV = 7;
	public static final int BRIGHTNESS = 8;
	public static final int NORMAL = 9;

	private int cmd = -1;
	protected float[] args;
	protected int arg;

	protected RenderCommand(int cmd) {
		this.cmd = cmd;
	}
	
	public int getCMD(){
		return cmd;
	}
	
	public float[] getArgs() {
		return args;
	}
	
	public int getArg() {
		return arg;
	}
	
	public abstract IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV);
}

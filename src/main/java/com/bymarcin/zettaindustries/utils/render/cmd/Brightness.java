package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;

import org.lwjgl.util.vector.Matrix4f;

import net.minecraft.client.renderer.Tessellator;

public class Brightness extends RenderCommand implements IRenderCommandExecutor{

	public Brightness(int brightness) {
		super(BRIGHTNESS);
		arg = brightness;
	}

	@Override
	public void execute(Tessellator tes) {
		tes.setBrightness(arg);		
	}

	@Override
	public IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV) {
		return this;
	}
}

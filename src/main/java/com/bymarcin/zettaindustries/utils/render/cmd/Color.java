package com.bymarcin.zettaindustries.utils.render.cmd;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;

import org.lwjgl.util.vector.Matrix4f;

import net.minecraft.client.renderer.Tessellator;

public class Color extends RenderCommand implements IRenderCommandExecutor{

	public Color(float r, float g, float b, float alpha) {
		this(r, g, b);
		args[3] = alpha;
	}

	public Color(float r, float g, float b) {
		super(COLOR);
		args = new float[] { r, g, b, 1 };
	}

	@Override
	public void execute(Tessellator tes, CustomModel model, boolean isInventory) {
		tes.setColorRGBA_F(args[0], args[1], args[2], args[3]);
	}

	@Override
	public IRenderCommandExecutor getExecutor(LinkedList<Matrix4f> transformations, float minU, float maxU, float minV, float maxV) {
		return this;
	}
}

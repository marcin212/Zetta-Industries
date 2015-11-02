package com.bymarcin.zettaindustries.utils.render.cmd.executor;

import com.bymarcin.zettaindustries.utils.render.CustomModel;

import net.minecraft.client.renderer.Tessellator;

public interface IRenderCommandExecutor {
	public void execute(Tessellator tes, CustomModel model, boolean isInventory);
}

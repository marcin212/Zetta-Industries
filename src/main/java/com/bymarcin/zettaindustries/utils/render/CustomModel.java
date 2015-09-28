package com.bymarcin.zettaindustries.utils.render;

import java.util.ArrayList;
import java.util.LinkedList;

import com.bymarcin.zettaindustries.utils.render.cmd.RenderCommand;
import com.bymarcin.zettaindustries.utils.render.cmd.executor.IRenderCommandExecutor;

import org.lwjgl.util.vector.Matrix4f;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

public class CustomModel {
	private LinkedList<RenderCommand> modelCommands = new LinkedList<RenderCommand>();
	private ArrayList<IRenderCommandExecutor> buffer = new ArrayList<IRenderCommandExecutor>();
	private float minU;
	private float minV;
	private float maxU;
	private float maxV;
	private boolean hasBuffer = false;

	public CustomModel() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public CustomModel dispose(){
		MinecraftForge.EVENT_BUS.unregister(this);
		return this;
	}
	
	public void generateBuffer() {
		LinkedList<Matrix4f> transformationMatrix = new LinkedList<Matrix4f>();
		transformationMatrix.add(Matrix4f.setIdentity(new Matrix4f()));
		for (RenderCommand cmd : modelCommands) {
			IRenderCommandExecutor executor = cmd.getExecutor(transformationMatrix, minU, maxU, minV, maxV);
			if (executor != null) {
				buffer.add(executor);
			}
		}
		hasBuffer = true;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void textureStich(TextureStitchEvent.Post event){
		clearBuffer();
	}

	public CustomModel addCommand(RenderCommand cmd) {
		modelCommands.add(cmd);
		return this;
	}

	public CustomModel create() {
		clearBuffer();
		generateBuffer();
		return this;
	}

	public void clearBuffer() {
		buffer.clear();
		hasBuffer = false;
	}

	public LinkedList<RenderCommand> getModelCommands() {
		return modelCommands;
	}

	public CustomModel setUV(float minU, float maxU, float minV, float maxV) {
		this.minU = minU;
		this.maxU = maxU;
		this.minV = minV;
		this.maxV = maxV;
		return this;
	}

	public void draw(Tessellator tesselator, IIcon icon) {
		if(!hasBuffer){
			setUV(icon.getMinU(), icon.getMaxU(), icon.getMinV(), icon.getMaxV()).create();
		}
		
		for (int i = 0; i < buffer.size(); i++) {
			buffer.get(i).execute(tesselator);
		}
	}
	

	public boolean hasBuffer(){
		return hasBuffer;
	}
	
	public float getMaxU() {
		return maxU;
	}

	public float getMinU() {
		return minU;
	}

	public float getMaxV() {
		return maxV;
	}

	public float getMinV() {
		return minV;
	}
}

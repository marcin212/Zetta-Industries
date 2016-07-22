package com.bymarcin.zettaindustries.mods.superconductor.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityControler;

public class GuiControler extends GuiContainer{
	private ResourceLocation guiTexture = new ResourceLocation(ZettaIndustries.MODID, "textures/gui/sc_gui.png");
	public final int xSize = 256;
	public final int ySize = 170;
	public TileEntityControler controler;
	public GuiControler(EntityPlayer player,TileEntityControler tile) {
		super(new ContainerControler(player,tile));
		controler = tile;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
		FluidTank tank = ((SuperConductor)controler.getMultiblockController()).tank;
		double percent = (float)tank.getFluidAmount()/(float)tank.getCapacity();
		Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
		drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);
		drawTexturedModalRect(posX+68, posY+29, 0, 175, (int)(percent*141.0D+0.5D), 71);
		fontRendererObj.drawString((int)(percent*100)+"%", posX+110, posY+130, 0x000);
		fontRendererObj.drawString(tank.getFluidAmount()+"/"+tank.getCapacity()+"mB", posX+110, posY+150, 0x000);
	}

}

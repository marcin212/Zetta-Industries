package com.bymarcin.zettaindustries.mods.battery.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityPowerTap;
import com.bymarcin.zettaindustries.registry.ZIRegistry;


public class GuiPowerTap extends GuiContainer{
	TileEntityPowerTap tile;
	GuiButton plus5;
	GuiButton minus5;
	
	GuiButton plus10;
	GuiButton minus10;
	
	GuiButton plus100;
	GuiButton minus100;
	
	GuiButton plus1000;
	GuiButton minus1000;
	
	private ResourceLocation guiTexture = new ResourceLocation(ZettaIndustries.MODID, "textures/gui/battery/guicube.png");
	public final int xSize = 176;
	public final int ySize = 88;
	
	public GuiPowerTap(Container arg0, TileEntityPowerTap tile) {
		super(arg0);
		this.tile = tile;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
							//id, x, y, width, height, text
        plus5 = new GuiButton(1, posX+10, posY+10, 20, 20, "+5");
        minus5 = new GuiButton(2, posX+10, posY+60, 20, 20, "-5");
        
        plus10 = new GuiButton(3, posX+35, posY+10, 25, 20, "+10");
        minus10 = new GuiButton(4, posX+35, posY+60, 25, 20, "-10");
        
        plus100 = new GuiButton(5, posX+65, posY+10, 30, 20, "+100");
        minus100 = new GuiButton(6, posX+65, posY+60, 30, 20, "-100");
        
        plus1000 = new GuiButton(7, posX+100, posY+10, 40, 20, "+1000");
        minus1000 = new GuiButton(8, posX+100, posY+60, 40, 20, "-1000");
        
        buttonList.add(minus5);
        buttonList.add(plus5);
        
        buttonList.add(minus10);
        buttonList.add(plus10);
        
        buttonList.add(minus100);
        buttonList.add(plus100);
        
        buttonList.add(minus1000);
        buttonList.add(plus1000);
        
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
		drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);
		fontRendererObj.drawString("Current transfer: "+String.valueOf(tile.getTransferCurrent())+" RF/t",posX+10, posY+10+20+10, 0);
		
		
	}

	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch(button.id){
		case 1:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+5,false)); break;
		case 2:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-5,false)); break;
		
		case 3:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+10,false)); break;
		case 4:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-10,false)); break;
		
		case 5:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+100,false)); break;
		case 6:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-100,false)); break;	
	
		case 7:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+1000,false)); break;
		case 8:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-1000,false)); break;		
		}
		
		super.actionPerformed(button);
	}
	
	
	
	
	
	
}

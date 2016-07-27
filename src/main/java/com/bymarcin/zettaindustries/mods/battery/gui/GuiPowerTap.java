package com.bymarcin.zettaindustries.mods.battery.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityPowerTap;
import com.bymarcin.zettaindustries.registry.ZIRegistry;

import java.io.IOException;


public class GuiPowerTap extends GuiContainer{
	TileEntityPowerTap tile;
	GuiButton plus1;
	GuiButton minus1;
	
	GuiButton plus10;
	GuiButton minus10;
	
	GuiButton plus100;
	GuiButton minus100;
	
	GuiButton plus1000;
	GuiButton minus1000;

    GuiButton plus10000;
    GuiButton minus10000;

    GuiButton plus100000;
    GuiButton minus100000;
    
    GuiTextField textfield;
    boolean isFocused = false;
    boolean needUpdate = false;
    
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
		posY+=14;
        plus1 = new GuiButton(1, posX+5, posY+10, 20, 20, "+1");
        minus1 = new GuiButton(2, posX+5, posY+60-14, 20, 20, "-1");
        
        plus10 = new GuiButton(3, posX+26, posY+10, 25, 20, "+10");
        minus10 = new GuiButton(4, posX+26, posY+60-14, 25, 20, "-10");
        
        plus100 = new GuiButton(5, posX+51, posY+10, 30, 20, "+100");
        minus100 = new GuiButton(6, posX+51, posY+60-14, 30, 20, "-100");
        
        plus1000 = new GuiButton(7, posX+82, posY+10, 25, 20, "+1K");
        minus1000 = new GuiButton(8, posX+82, posY+60-14, 25, 20, "-1K");

        plus10000 = new GuiButton(9, posX+107, posY+10, 29, 20, "+10K");
        minus10000 = new GuiButton(10, posX+107, posY+60-14, 29, 20, "-10K");

        plus100000 = new GuiButton(11, posX+136, posY+10, 36, 20, "+100K");
        minus100000 = new GuiButton(12, posX+136, posY+60-14, 36, 20, "-100K");
        
        textfield = new GuiTextField(0,fontRendererObj, posX+51, posY-7, 120, 14);
        textfield.setText(tile.getLabel());
        
        buttonList.add(minus1);
        buttonList.add(plus1);
        
        buttonList.add(minus10);
        buttonList.add(plus10);
        
        buttonList.add(minus100);
        buttonList.add(plus100);
        
        buttonList.add(minus1000);
        buttonList.add(plus1000);

        buttonList.add(plus10000);
        buttonList.add(minus10000);

        buttonList.add(plus100000);
        buttonList.add(minus100000);

		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
		drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);
		fontRendererObj.drawString("Current transfer: "+String.valueOf(tile.getTransferCurrent())+" RF/t",posX+10, posY+10+20+10+8, 0);
		fontRendererObj.drawString("Name: ",posX+10, posY+10, 0);
		textfield.drawTextBox();
		if(textfield.isFocused() && !isFocused){
			isFocused = true;
		}else if(!textfield.isFocused() && isFocused){
			isFocused = false;
			if(!tile.getLabel().equals(textfield.getText()))
				needUpdate = true;
		}
		
	}

	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id){
		case 1:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+1,false,textfield.getText())); break;
		case 2:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-1,false,textfield.getText())); break;
		
		case 3:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+10,false,textfield.getText())); break;
		case 4:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-10,false,textfield.getText())); break;
		
		case 5:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+100,false,textfield.getText())); break;
		case 6:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-100,false,textfield.getText())); break;	
	
		case 7:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+1000,false,textfield.getText())); break;
		case 8:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-1000,false,textfield.getText())); break;

        case 9:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+10000,false,textfield.getText())); break;
        case 10:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-10000,false,textfield.getText())); break;

        case 11:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()+100000,false,textfield.getText())); break;
        case 12:ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent()-100000,false,textfield.getText())); break;

        }
		super.actionPerformed(button);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		ZIRegistry.packetHandler.sendToServer(new PowerTapUpdatePacket(tile, tile.getTransferCurrent(),false,textfield.getText()));
	}
	
	@Override
	protected void keyTyped(char sign, int hm) throws IOException {
		if(!textfield.textboxKeyTyped(sign, hm)){
			super.keyTyped(sign, hm);
		}
		
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		textfield.mouseClicked(x, y, button);
	}
	
	
	
}

package com.bymarcin.zettaindustries.mods.simpledhd.gui;

import com.bymarcin.zettaindustries.mods.simpledhd.network.GuiActionPacket;
import com.bymarcin.zettaindustries.mods.simpledhd.tileentity.TileEntitySimpleDHD;
import com.bymarcin.zettaindustries.registry.ZIRegistry;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiSimpleDHD extends GuiScreen {
	private GuiTextField address;
	private GuiButton connect;
	private GuiButton disconnect;
	private TileEntitySimpleDHD te;
	
	public GuiSimpleDHD(TileEntitySimpleDHD te) {
		this.te=te;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		int centerh= width/2;
		int centerv= height/2;
		
		Keyboard.enableRepeatEvents(true);
		address = new GuiTextField(this.fontRendererObj, centerh-75, centerv-7, 150, 14);
		connect = new GuiButton(1, centerh-75, centerv+10, 70, 20, "Dial");
		disconnect = new GuiButton(2, centerh+5, centerv+10, 70, 20, "Disconnect");
		buttonList.add(connect);
		buttonList.add(disconnect);
		
		super.initGui();
	}
	
	protected void actionPerformed(GuiButton button) {
		switch(button.id){
		case 1: ZIRegistry.packetHandler.sendToServer(new GuiActionPacket(te, address.getText(), true));
			break;
		case 2: ZIRegistry.packetHandler.sendToServer(new GuiActionPacket(te, address.getText(), false));
			break;
		}
	}  
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char eventCharacter, int eventKey) {
		if(!address.textboxKeyTyped(eventCharacter, eventKey)){
			super.keyTyped(eventCharacter, eventKey);
		}
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int mouseButton) {
		this.address.mouseClicked(par1, par2, mouseButton);
		super.mouseClicked(par1, par2, mouseButton);
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		address.drawTextBox();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
	
	@Override
    public void updateScreen() {
		super.updateScreen();
    	address.updateCursorCounter();
    }

}

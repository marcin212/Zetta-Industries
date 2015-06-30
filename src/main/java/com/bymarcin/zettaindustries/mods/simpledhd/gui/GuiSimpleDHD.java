package com.bymarcin.zettaindustries.mods.simpledhd.gui;

import com.bymarcin.zettaindustries.mods.simpledhd.network.GuiActionPacket;
import com.bymarcin.zettaindustries.mods.simpledhd.tileentity.TileEntitySimpleDHD;
import com.bymarcin.zettaindustries.registry.ZIRegistry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiSimpleDHD extends GuiScreen {
	private GuiTextField address;
	private boolean addressFocus;
	TileEntitySimpleDHD te;
	private GuiButton connect;
	private GuiButton disconnect;
	
	public GuiSimpleDHD(TileEntitySimpleDHD te) {
		this.te = te;
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
		
		if (te !=null && !te.getAddress().isEmpty()) {
			address.setText(te.getAddress());
		}
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
	
	public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char eventCharacter, int eventKey) {
		if (this.address.isFocused()) {
			if (eventKey == Keyboard.KEY_RETURN) {
				this.address.setFocused(false);
			} else {
				this.address.textboxKeyTyped(eventCharacter, eventKey);
			}
			return;
		}
		super.keyTyped(eventCharacter, eventKey);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int mouseButton) {
		this.address.mouseClicked(par1, par2, mouseButton);
		super.mouseClicked(par1, par2, mouseButton);
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (addressFocus != address.isFocused()) {
			updateText();
		}
		addressFocus = address.isFocused();
		address.drawTextBox();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}


	private void updateText(){
		te.setAddress(this.address.getText());
	}
	
	@Override
    public void updateScreen() {
    	address.updateCursorCounter();
    }

}

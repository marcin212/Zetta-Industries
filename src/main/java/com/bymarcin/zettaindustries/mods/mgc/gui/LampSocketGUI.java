package com.bymarcin.zettaindustries.mods.mgc.gui;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class LampSocketGUI extends GuiContainer{

	public LampSocketGUI(InventoryPlayer inventoryPlayer, LampSocketTileEntity tileEntity) {
		super(new LampSocketContainer(inventoryPlayer, tileEntity));
		// TODO Auto-generated constructor stub
	}

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
            //draw text and stuff here
            //the parameters for drawString are: string, x, y, color
    	fontRendererObj.drawString("LampSocket", 8, 6, 4210752);
            //draws "Inventory" or your regional equivalent
    	fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 130 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                    int par3) {
            //draw your Gui here, only thing you need to change is the path
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.bindTexture(new ResourceLocation(ZettaIndustries.MODID + ":textures/gui/mgc/lampsocket.png"));
            int x = (width - xSize) / 2;
            int y = (height - ySize) / 2;
            this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}

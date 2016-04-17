package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.nfc.NFC;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.util.ForgeDirection;

public class SmartCardBlockTerminalRenderer extends TileEntitySpecialRenderer{
	ResourceLocation rl = new ResourceLocation(ZettaIndustries.MODID+":textures/blocks/nfc/terminal_front_lights.png");
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		SmartCardTerminalTileEntity terminal = (SmartCardTerminalTileEntity)te;
		if(terminal.renderInfo==null) return;
		if(!terminal.renderInfo.getBoolean("hasCard"))return;
		
		if(!(terminal.getBlockMetadata()<4)) return;
		ForgeDirection facing = ForgeDirection.VALID_DIRECTIONS[SmartCardTerminalBlock.sides[terminal.getBlockMetadata()]];
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		

        switch(facing) {
        case WEST: GL11.glRotatef(-90, 0, 1, 0); break;
        case NORTH: GL11.glRotatef(180, 0, 1, 0); break;
        case EAST:  GL11.glRotatef(90, 0, 1, 0); break;
        default:;
      }
		
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 4.5f / 16, 10 / 16f);
        GL11.glRotatef(90, -1, 0, 0);
      
        int brightness = terminal.getWorldObj().getLightBrightnessForSkyBlocks(terminal.xCoord + facing.offsetX, terminal.yCoord + facing.offsetY, terminal.zCoord + facing.offsetZ, 0);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness % 65536, brightness / 65536);

        // This is very 'meh', but item frames do it like this, too!
        EntityItem entity = new EntityItem(terminal.getWorldObj(), 0, 0, 0, new ItemStack(NFC.smartCardItem));
        entity.hoverStart = 0;
        RenderItem.renderInFrame = true;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0, 0, 0, 0, 0);
        RenderItem.renderInFrame = false;
        GL11.glPopMatrix();

        
        GL11.glColor3d(0, 1, 0);
        GL11.glTranslated(-0.5, 0.5, 0.5005);
        GL11.glScalef(1, -1, 1);
        bindTexture(rl);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(0, 1, 0, 0, 1);
        t.addVertexWithUV(5/16f, 1, 0, 5/16f, 1);
        t.addVertexWithUV(5/16f, 0, 0, 5/16f, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();

        
        
		if (terminal.renderInfo.getBoolean("validOwner")) {
			if (terminal.renderInfo.getBoolean("isProtected")) {
				GL11.glColor3d(0, 1, 0);
			} else {
				GL11.glColor3d(254 / 255f, 196 / 255f, 54 / 255f);
			}
		} else {
			GL11.glColor3d(1, 0, 0);
		}
        t.startDrawingQuads();
        t.addVertexWithUV(5/16f, 1, 0, 5/16f, 1);
        t.addVertexWithUV(1, 1, 0, 1, 1);
        t.addVertexWithUV(1, 0, 0, 1, 0);
        t.addVertexWithUV(5/16f, 0, 0, 5/16f, 0);
        t.draw();
        
        
        GL11.glColor3d(1, 1, 1);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        
	}

}

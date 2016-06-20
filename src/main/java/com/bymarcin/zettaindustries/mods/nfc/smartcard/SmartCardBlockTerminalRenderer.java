package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.nfc.NFC;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SmartCardBlockTerminalRenderer extends TileEntitySpecialRenderer<SmartCardTerminalTileEntity>{
	ResourceLocation rl = new ResourceLocation(ZettaIndustries.MODID+":textures/blocks/nfc/terminal_front_lights.png");
    public static final int[] sides = {3,4,2,5};
    @Override
    public void renderTileEntityAt(SmartCardTerminalTileEntity terminal, double x, double y, double z, float partialTicks, int destroyStage) {
        System.out.println("WAT?");
        if(terminal.renderInfo==null) return;

       // if(!terminal.renderInfo.getBoolean("hasCard"))return;

        if(!(terminal.getBlockMetadata()<4)) return;

        EnumFacing facing = EnumFacing.values()[sides[terminal.getBlockMetadata()]];
        //GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);


        switch(facing) {
            case WEST: GlStateManager.rotate(-90, 0, 1, 0); break;
            case NORTH: GlStateManager.rotate(180, 0, 1, 0); break;
            case EAST:  GlStateManager.rotate(90, 0, 1, 0); break;
            default:;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 4.5f / 16, 5 / 16f);
        GlStateManager.rotate(90, -1, 0, 0);

        int brightness = terminal.getWorld().getCombinedLight(new BlockPos(
                terminal.getPos().getX() + facing.getFrontOffsetX(),
                terminal.getPos().getY() + facing.getFrontOffsetY(),
                terminal.getPos().getZ() + facing.getFrontOffsetZ()
        ),0);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness % 65536, brightness / 65536);

        // This is very 'meh', but item frames do it like this, too!
        EntityItem entity = new EntityItem(terminal.getWorld(), 0, 0, 0, new ItemStack(NFC.smartCardItem));
        entity.hoverStart = 0;

        Minecraft.getMinecraft().getRenderItem().renderItem(entity.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);

        GlStateManager.popMatrix();


        GlStateManager.color(0f, 1f, 0f);
        GlStateManager.translate(-0.5, 0.5, 0.5005);
        GlStateManager.scale(1, -1, 1);
        bindTexture(rl);
        VertexBuffer t = Tessellator.getInstance().getBuffer();
        t.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        t.pos(0, 1, 0)     .tex(0, 1).endVertex();
        t.pos(5/16f, 1, 0) .tex(5/16f, 1).endVertex();
        t.pos(5/16f, 0, 0) .tex(5/16f, 0).endVertex();
        t.pos(0, 0, 0)     .tex(0, 0).endVertex();

        Tessellator.getInstance().draw();


        if (terminal.renderInfo.getBoolean("validOwner")) {
            if (terminal.renderInfo.getBoolean("isProtected")) {
                GlStateManager.color(0f, 1f, 0f);
            } else {
                GlStateManager.color(254 / 255f, 196 / 255f, 54 / 255f);
            }
        } else {
            GlStateManager.color(1f, 0f, 0f);
        }

        t.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        t.pos(5/16f, 1, 0) .tex(5/16f, 1).endVertex();
        t.pos(1, 1, 0)     .tex(1, 1).endVertex();
        t.pos(1, 0, 0)     .tex(1, 0).endVertex();
        t.pos(5/16f, 0, 0) .tex(5/16f, 0).endVertex();
        Tessellator.getInstance().draw();

        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();
        //GL11.glPopAttrib();
    }
}

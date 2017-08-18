package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.nfc.NFC;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import li.cil.oc.api.event.RackMountableRenderEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


import net.minecraftforge.client.event.TextureStitchEvent;

public class SmartCardRackRenderer {
	TextureAtlasSprite texture;
	ResourceLocation texture_lights = new ResourceLocation(ZettaIndustries.MODID, "textures/blocks/nfc/smart_card_terminal_lights.png");
	
	
	@SubscribeEvent
	public void textureHook(TextureStitchEvent.Pre e) {
		texture = e.getMap().registerSprite(new ResourceLocation(ZettaIndustries.MODID, "/blocks/nfc/smart_card_terminal"));
	}

	@SubscribeEvent
	public void onRackMountableRender(RackMountableRenderEvent.Block e) {
		ItemStack stack = e.rack.getStackInSlot(e.mountable);
		if (!stack.isEmpty() && stack.getItem() instanceof SmartCardTerminalItem) {
			e.setFrontTextureOverride(texture);
		}
	}

	@SubscribeEvent
	public void onRackMountableRender(RackMountableRenderEvent.TileEntity e) {
		ItemStack stack = e.rack.getStackInSlot(e.mountable);
		if (stack.getItem() instanceof SmartCardTerminalItem && e.data != null) {
			Minecraft mc = Minecraft.getMinecraft();
			TextureManager tm = mc.getTextureManager();
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

			if (e.data.getBoolean("hasCard")) {
				GL11.glPushMatrix();
				GL11.glScalef(1, -1, 1);
				GL11.glTranslatef(11.5f / 16f, -(3.5f + e.mountable * 3f) / 16f, -2.5f / 16f);
				GL11.glScalef(0.7f, 0.7f, 0.7f);
				GL11.glRotatef(90, -1, 0, 0);
				int brightness = e.rack.world().getCombinedLight(new BlockPos(
						(int) e.rack.xPosition() + e.rack.facing().getFrontOffsetX(),
						(int) e.rack.yPosition() + e.rack.facing().getFrontOffsetY(),
						(int) e.rack.zPosition() + e.rack.facing().getFrontOffsetZ()), 0);
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness % 65536, brightness / 65536);
				// This is very 'meh', but item frames do it like this, too!
				EntityItem entity = new EntityItem(e.rack.world(), 0, 0, 0, new ItemStack(NFC.smartCardItem));
				entity.hoverStart = 0;


				Minecraft.getMinecraft().getRenderItem().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.FIXED);

				GL11.glPopMatrix();
				GL11.glColor3d(0, 1, 0);
				e.renderOverlay(texture_lights, 5 / 16f, 7 / 16f);

				if (e.data.getBoolean("validOwner")) {
					if (e.data.getBoolean("isProtected")) {
						GL11.glColor3d(0, 1, 0);
					} else {
						GL11.glColor3d(254 / 255f, 196 / 255f, 54 / 255f);
					}
				} else {
					GL11.glColor3d(1, 0, 0);
				}
				e.renderOverlay(texture_lights, 0 / 16f, 4 / 16f);
			}
			GL11.glColor3d(1, 1, 1);
			GL11.glPopAttrib();
		}

	}

}

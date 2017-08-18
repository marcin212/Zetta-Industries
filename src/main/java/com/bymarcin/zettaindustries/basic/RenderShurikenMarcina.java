package com.bymarcin.zettaindustries.basic;

import com.bymarcin.zettaindustries.ZettaIndustries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderShurikenMarcina extends Render<EntityShurikenMarcina> {
    private static final float ROTATION_SPEED = 50f;
    private final RenderItem itemRender;

    public RenderShurikenMarcina(RenderManager renderManager) {
        super(renderManager);
        itemRender = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void doRender(EntityShurikenMarcina entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(90.0F, 1, 0, 0);
        GlStateManager.rotate((entity.ticksExisted + partialTicks) * ROTATION_SPEED, 0, 0, 1);
        this.bindTexture(getEntityTexture(entity));

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        itemRender.renderItem(new ItemStack(ZettaIndustries.itemLogo), ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityShurikenMarcina entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

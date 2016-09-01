package com.bymarcin.zettaindustries.utils.render;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Marcin on 31.08.2016.
 */
public class CustomFaceBakery extends FaceBakery {
    public static final CustomFaceBakery INSTANCE = new CustomFaceBakery();

    public BakedQuad makeBakedQuad(Vector3f min, Vector3f max, int tintIndex,
                                   TextureAtlasSprite icon, EnumFacing facing, ModelRotation rot, boolean uvLocked) {
        return makeBakedQuad(min, max, tintIndex, RenderUtils.calculateUV(min, max, facing), icon, facing, rot, uvLocked);
    }

    public BakedQuad makeBakedQuad(Vector3f min, Vector3f max, int tintIndex, float[] uv,
                                   TextureAtlasSprite icon, EnumFacing facing, ModelRotation rot, boolean uvLocked) {
        boolean hasColorIndex = tintIndex != -1 && ((tintIndex & 0xFF000000) == 0);
        boolean hasColor = tintIndex != -1 && ((tintIndex & 0xFF000000) != 0);

        BakedQuad quad = makeBakedQuad(
                min, max,
                new BlockPartFace(null, hasColorIndex ? tintIndex : -1, "", new BlockFaceUV(uv, 0)),
                icon, facing, rot, null, uvLocked, true
        );

        if (hasColor) {
            RenderUtils.recolorQuad(quad, tintIndex);
        }

        return quad;
    }
}

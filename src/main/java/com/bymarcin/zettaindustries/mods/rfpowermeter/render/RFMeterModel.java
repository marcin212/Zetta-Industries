package com.bymarcin.zettaindustries.mods.rfpowermeter.render;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.utils.render.BaseBakedModel;
import com.bymarcin.zettaindustries.utils.render.CustomFaceBakery;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Marcin on 31.08.2016.
 */
public class RFMeterModel extends BaseBakedModel {
    List<BakedQuad> quadsBackground = new LinkedList<>();
    List<BakedQuad> quadsNumberUP = new LinkedList<>();
    List<BakedQuad> quadsNumberDown = new LinkedList<>();
    List<BakedQuad> finalQuads = new LinkedList<>();
    long numberUP;
    long numberDOWN;
    public static ResourceLocation cTexture1 = new ResourceLocation(ZettaIndustries.MODID,"blocks/counter");
    ModelRotation rotation;
    ModelRotation rotationOpposite;
    TextureAtlasSprite texture;
    BakedQuad[] si = new BakedQuad[RFMeterRender.SI.values().length - 1];
    BakedQuad[] direction = new BakedQuad[2];
    int color;
    int colorDark;


    public RFMeterModel(EnumFacing facing, EnumDyeColor color) {
        this.texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(cTexture1.toString());
        this.color = color.getColorValue();
        this.color = 0XFF000000 | ((this.color >> 16 & 255)) | ((this.color >> 8 & 255)<<8) | ((this.color & 255)<<16);
        colorDark = 0XFF000000 | ((int)((this.color >> 16 & 255)*0.3d)<<16) | ((int)((this.color >> 8 & 255)*0.3d)<<8) | ((int)((this.color & 255)*0.3d));


        switch (facing){
            case WEST: rotation = ModelRotation.X0_Y0; rotationOpposite = ModelRotation.X0_Y180; break;
            case EAST: rotation = ModelRotation.X0_Y180; rotationOpposite = ModelRotation.X0_Y0; break;
            case NORTH: rotation = ModelRotation.X0_Y90; rotationOpposite = ModelRotation.X0_Y270; break;
            case SOUTH: rotation = ModelRotation.X0_Y270; rotationOpposite = ModelRotation.X0_Y90; break;
        }

        // up LCD background
        quadsBackground.add(
                CustomFaceBakery.INSTANCE.makeBakedQuad(new Vector3f(2.999f,12,4), new Vector3f(2.999f,14,12),this.color,
                        new float[] {0, 11f/4f,
                                43f/4f, 22f/4f},
                        texture, EnumFacing.WEST, rotation, true)
        );

        // down LCD background
        quadsBackground.add(
                CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,9,4), new Vector3f(2.999f,11,12),this.color,
                        new float[] {0, 11f/4f,
                                43f/4f, 22f/4f},
                        texture, EnumFacing.WEST, rotation, true)
        );

        // SI background
        quadsBackground.add(
                CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,5,4), new Vector3f(2.999f,7,12),colorDark,
                        new float[] {26f/4f, 40f/4f,
                                55f/4f, 47f/4f},
                        texture, EnumFacing.WEST, rotation, true)
        );

        cacheSI();

        // Direction background
        float[] up=new float[] {56f/4f, 40f/4f, 62f/4f, 47f/4f};
        float[] down=new float[] {62f/4f, 47f/4f, 56f/4f, 40f/4f};
        direction[0] = CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,2,7), new Vector3f(2.999f,4,9),this.color,
                down, texture, EnumFacing.WEST, rotation, false);
        direction[1] =
                CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,2,7), new Vector3f(2.999f,4,9),this.color,
                        up, texture, EnumFacing.WEST, rotation, false);
    }


    private void cacheSI(){
        float siSize = 6f / 4f;
        float startTex = 26f / 4f;
        for (RFMeterRender.SI si : RFMeterRender.SI.values()) {
            if(si == RFMeterRender.SI.none) continue;
            float textMin = startTex + siSize * (RFMeterRender.SI.K.ordinal() - si.ordinal());
            this.si[si.ordinal()] =
                    CustomFaceBakery.INSTANCE.makeBakedQuad(
                            new Vector3f(2.998f, 5, 4 + 1.655f * (RFMeterRender.SI.K.ordinal() - si.ordinal())),
                            new Vector3f(2.998f, 7, 4 + 1.655f * (RFMeterRender.SI.K.ordinal() - si.ordinal() + 1)),
                            this.color,
                            new float[]{textMin, 40f / 4f,
                                    textMin + siSize, 47f / 4f},
                            texture, EnumFacing.WEST, rotation, true);
        }
    }

    public void setNumber(long numberUP, long numberDOWN, RFMeterRender.SI si, boolean inverted) {
        this.numberUP = numberUP;
        this.numberDOWN = numberDOWN;
        finalQuads.clear();

        generateNumber(quadsNumberUP, numberUP, false, 9);
        generateNumber(quadsNumberDown, numberDOWN, true, 12);
        if (si!= RFMeterRender.SI.none)
            finalQuads.add(this.si[si.ordinal()]);
        if (inverted)
            finalQuads.add(direction[1]);
        else
            finalQuads.add(direction[0]);

        finalQuads.addAll(quadsBackground);
        finalQuads.addAll(quadsNumberUP);
        finalQuads.addAll(quadsNumberDown);
    }

    public List<BakedQuad> generateNumber(List<BakedQuad> quads, long number, boolean dot, float posFrom){
        quads.clear();
        float offset = 3.8f;

        for(int i=0;number != 0 || i<(dot?1:2);number = number/10,i++){
            long dig = number%10;
            long x = dig *6;

            quads.add(
                    CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(13.002f,posFrom,offset), new Vector3f(13.002f,posFrom+2,offset + 4.5f/4f),this.color,
                            new float[] {x/4f, 0, (x+6)/4f, 11/4f},
                            texture, EnumFacing.EAST, rotationOpposite, true)
            );

            offset += 4.5f/4f;
            if(i==0){
                if(dot){
                    quads.add(
                            CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(13.002f,9,offset), new Vector3f(13.002f,11,offset + 1.5f/4f),this.color,
                                    new float[] {60f/4f, 0, 62f/4f, 11/4f},
                                    texture, EnumFacing.EAST, rotationOpposite, true)
                    );
                }
                offset += 1.5f/4f;
            }
        }

        return quads;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side == EnumFacing.NORTH)
            return finalQuads;
        else
            return Collections.EMPTY_LIST;
    }
}

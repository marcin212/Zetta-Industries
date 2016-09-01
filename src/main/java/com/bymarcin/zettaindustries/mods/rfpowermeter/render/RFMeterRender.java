package com.bymarcin.zettaindustries.mods.rfpowermeter.render;

import com.bymarcin.zettaindustries.mods.rfpowermeter.RFMeterBlock;
import com.bymarcin.zettaindustries.utils.render.BaseBakedModel;
import com.bymarcin.zettaindustries.utils.render.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.animation.FastTESR;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.rfpowermeter.RFMeterTileEntity;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import javax.vecmath.*;
import java.util.LinkedList;
import java.util.List;

public class RFMeterRender extends FastTESR<RFMeterTileEntity>//implements IItemRenderer
{
	enum SI {
		P(50, 40),
		T(44, 40),
		G(38, 40),
		M(32, 40),
		K(26, 40),
		none(0, 0);
		int x, y, width, height;
		public static final SI[] reverse = valuesReverse();

		SI(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		SI(int x, int y) {
			this(x, y, 5, 7);
		}

		private static SI[] valuesReverse() {
			int len = SI.values().length;
			SI[] arr = new SI[len];
			for (SI si : values()) {
				arr[len - si.ordinal() - 1] = si;
			}
			return arr;
		}
	}

	public static RFMeterRender render = new RFMeterRender();
	RFMeterModel[] model;

	@Override
	public void renderTileEntityFast(RFMeterTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer vertexBuffer) {
		EnumFacing facing = tile.getWorld().getBlockState(tile.getPos()).getValue(RFMeterBlock.front);
		BlockPos pos = tile.getPos();

		if(model==null) {
			model = new RFMeterModel[EnumFacing.HORIZONTALS.length];
			for (int i = 0; i < EnumFacing.HORIZONTALS.length; i++) {
				model[i] = new RFMeterModel(EnumFacing.HORIZONTALS[i]);
			}
		}

		long total = tile.getCurrentValue();
		int unit=0;
		total *=10;
		while(Math.ceil(Math.log10(total))>6){
			unit++;
			total /=1000;
		}

		SI si = SI.reverse[unit];


		RFMeterModel m = model[facing.getHorizontalIndex()];
		m.setNumber(total,tile.getTransfer(), si, tile.isInverted());

		vertexBuffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tile.getWorld(),m, tile.getWorld().getBlockState(tile.getPos()), tile.getPos(), vertexBuffer, false);

	}
}

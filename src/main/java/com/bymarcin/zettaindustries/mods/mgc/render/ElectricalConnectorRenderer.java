package com.bymarcin.zettaindustries.mods.mgc.render;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.ElectricPoleTiered;
import com.bymarcin.zettaindustries.mods.mgc.block.ElectricalConnectorBlock;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalConnectorTileEntity;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;
import com.cout970.magneticraft.api.electricity.IElectricPole;
import com.cout970.magneticraft.api.electricity.IInterPoleWire;
import com.cout970.magneticraft.api.electricity.ITileElectricPole;
import com.cout970.magneticraft.api.util.VecDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.ImmersiveEngineering;

public class ElectricalConnectorRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{
	public static ResourceLocation ELECTRIC_WIRE_TIER_1 = new ResourceLocation(ZettaIndustries.MODID,"textures/items/mgc/wire_tier1.png");
	public static int renderid = RenderingRegistry.getNextAvailableRenderId();
	
	public static ResourceLocation[] rl = new ResourceLocation[4];
	CustomModel[][] models = new CustomModel[4][6];
	
	public ElectricalConnectorRenderer() {
		rl[0] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/connectorLV.obj");
		rl[1] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/connectorMV.obj");
		rl[2] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/connectorHV.obj");
		rl[3] = new ResourceLocation(ImmersiveEngineering.MODID + ":models/relayHV.obj");
		init();
	}
	
	private void init(){
		//public static final ForgeDirection[] VALID_DIRECTIONS = {DOWN, UP, NORTH, SOUTH, WEST, EAST};
		for (int j = 0; j < rl.length; j++) {
			float angle = 0;
			for (int i = 0; i < 6; i++) {
					int x =0;
					int y =0;
					int z =0;
					float a = angle;
					
					if(i==1){
						x=1;
						a = 180;
					}
					
					if(i==2){
						x=1;
						a=90;
					}
					
					if(i==3){
						x=1;
						a=-90;
					}
					
					if(i==4){
						z=1;
						a=-90;
					}
					
					if(i==5){
						z=1;
						a=90;
					}
					
					models[j][i] = CustomModelFactory.rotateModel(a, x, y, z, CustomModelFactory.translateModel(0.5f, 0.5f, 0.5f, CustomModelFactory.createModel(rl[j]).dispose()).dispose());
				angle -= 90;
			}
		}
	}
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
//		GL11.glPushMatrix();
//		GL11.glTranslatef((float) x + 0.5F, (float) y - 2.5F, (float) z + 0.5F);
//		GL11.glColor4f(1, 1, 1, 1);
//		GL11.glPopMatrix();
		ElectricPoleTiered pole1 = (ElectricPoleTiered) ((ITileElectricPole) te).getPoleConnection();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glColor4f(1, 1, 1, 1);
		if (pole1.glList == -1) {
			pole1.glList = GL11.glGenLists(1);
			GL11.glNewList(pole1.glList, GL11.GL_COMPILE_AND_EXECUTE);

			int count = pole1.getWireConnectors().length;
			for (IInterPoleWire wire : pole1.getConnectedConductors()) {
				if(wire.getWorld() == null) continue;
				if (wire.getStart() != pole1)
					continue;
				IElectricPole pole2 = wire.getEnd();
				if (pole2 == null || pole2.getWireConnectors().length != count) {
					continue;
				}
				VecDouble off = new VecDouble(-te.xCoord, -te.yCoord, -te.zCoord);

				VecDouble dist = new VecDouble(pole2.getParent()).add(off);
				bindTexture(ELECTRIC_WIRE_TIER_1);
				VecDouble[] startConnectors = pole1.getWireConnectors();
				VecDouble[] endConnectors = pole2.getWireConnectors();
//				if (Math.abs(te.getBlockMetadata() - pole2.getParent().getBlockMetadata()) > 3) {
//					ArrayUtils.reverse(endConnectors);
//				}
				for (int i = 0; i < count; i++) {
					VecDouble a = startConnectors[i], b = endConnectors[i];
					b.add(dist);// b relative to a
					VecDouble ab = b.copy().add(a.getOpposite());// (b-a)
					double lenght = ab.mag();// distance between a and b
					VecDouble mid = ab.copy(); // (b-a)
					mid.multiply(0.5).add(a);// (b-a)/2 + a
					double lowPoint = mid.getY() - lenght * 0.05;// height of
																	// the
																	// middel
																	// point
																	// (-weight)
					double quallity = 8;
					Tessellator t = Tessellator.instance;
					t.startDrawingQuads();
					for (int p = 0; p < quallity; p++) {
						double adv1 = p / quallity, adv2 = (p + 1) / quallity;
						drawLine(new VecDouble(a.getX() + ab.getX() * adv1, interpolate(a.getY(), lowPoint, b.getY(), adv1), a.getZ() + ab.getZ() * adv1),
								new VecDouble(a.getX() + ab.getX() * adv2, interpolate(a.getY(), lowPoint, b.getY(), adv2), a.getZ() + ab.getZ() * adv2),
								0.0625F * 0.5F);
					}
					t.draw();
				}
			}
			GL11.glEndList();
		} else {
			GL11.glCallList(pole1.glList);
		}
		GL11.glPopMatrix();
	}

	public static double interpolate(double fa, double fb, double fc, double x) {
		double a = 0, b = 0.5, c = 1;
		double L0 = ((x - b) / (a - b)) * ((x - c) / (a - c));
		double L1 = ((x - a) / (b - a)) * ((x - c) / (b - c));
		double L2 = ((x - a) / (c - a)) * ((x - b) / (c - b));
		return fa * L0 + fb * L1 + fc * L2;
	}

	public static void drawLine(VecDouble a, VecDouble b, float f) {
		Tessellator t = Tessellator.instance;
		float w = f / 2;
		t.addVertex(a.getX(), a.getY() - w, a.getZ());
		t.addVertex(a.getX(), a.getY() + w, a.getZ());
		t.addVertex(b.getX(), b.getY() + w, b.getZ());
		t.addVertex(b.getX(), b.getY() - w, b.getZ());

		t.addVertex(a.getX(), a.getY(), a.getZ() - w);
		t.addVertex(a.getX(), a.getY(), a.getZ() + w);
		t.addVertex(b.getX(), b.getY(), b.getZ() + w);
		t.addVertex(b.getX(), b.getY(), b.getZ() - w);

		t.addVertex(a.getX() - w, a.getY(), a.getZ());
		t.addVertex(a.getX() + w, a.getY(), a.getZ());
		t.addVertex(b.getX() + w, b.getY(), b.getZ());
		t.addVertex(b.getX() - w, b.getY(), b.getZ());
		// inverted
		t.addVertex(a.getX(), a.getY() + w, a.getZ());
		t.addVertex(a.getX(), a.getY() - w, a.getZ());
		t.addVertex(b.getX(), b.getY() - w, b.getZ());
		t.addVertex(b.getX(), b.getY() + w, b.getZ());

		t.addVertex(a.getX(), a.getY(), a.getZ() + w);
		t.addVertex(a.getX(), a.getY(), a.getZ() - w);
		t.addVertex(b.getX(), b.getY(), b.getZ() - w);
		t.addVertex(b.getX(), b.getY(), b.getZ() + w);

		t.addVertex(a.getX() + w, a.getY(), a.getZ());
		t.addVertex(a.getX() - w, a.getY(), a.getZ());
		t.addVertex(b.getX() - w, b.getY(), b.getZ());
		t.addVertex(b.getX() + w, b.getY(), b.getZ());

	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator.instance.startDrawingQuads();
		models[metadata][0].drawInventory(Tessellator.instance, block.getIcon(0, metadata));
		Tessellator.instance.draw();
	}


	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		//init();
		int meta = world.getBlockMetadata(x, y, z);
		if(!(world.getTileEntity(x, y, z) instanceof ElectricalConnectorTileEntity))
			return true;
		ElectricalConnectorTileEntity te = (ElectricalConnectorTileEntity) world.getTileEntity(x, y, z);
		int lb = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int lb_j = lb % 65536;
		int lb_k = lb / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lb_j / 1.0F, (float) lb_k / 1.0F);
		Tessellator tessellator = Tessellator.instance;
		tessellator.addTranslation(x, y, z);
		models[meta][meta==ElectricalConnectorBlock.relayHV?ForgeDirection.getOrientation(te.getFacing()).getOpposite().ordinal():te.getFacing()].draw(tessellator, block.getIcon(0, meta), world, x, y, z, renderer);
		tessellator.addTranslation(-(x), -(y), -(z));
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderid;
	}
}

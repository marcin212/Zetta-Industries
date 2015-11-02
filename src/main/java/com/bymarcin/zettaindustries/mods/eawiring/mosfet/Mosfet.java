package com.bymarcin.zettaindustries.mods.eawiring.mosfet;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.registry.ICustomHighlight;
import com.bymarcin.zettaindustries.utils.render.CustomModel;
import com.bymarcin.zettaindustries.utils.render.CustomModelFactory;

import mods.eln.Eln;
import mods.eln.ghost.GhostElement;
import mods.eln.misc.Coordonate;
import mods.eln.misc.Direction;
import mods.eln.node.simple.SimpleNode;
import mods.eln.node.simple.SimpleNodeBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Mosfet extends SimpleNodeBlock implements ICustomHighlight {
	public static int renderid = RenderingRegistry.getNextAvailableRenderId();
	
	AxisAlignedBB[] box = new AxisAlignedBB[]{
			AxisAlignedBB.getBoundingBox(5/16f, 3/16f, 5/16f, 11/16f, 11/16f, 11/16f),
			AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2/16f, 1)
	};
	

	
	public Mosfet() {
		super(Material.iron);
		setBlockName("Mosfet");
		setHardness(3.0F);
		setDescriptor(new MosfetDesc());
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		
		textureName = ZettaIndustries.MODID + ":eawires/Mosfet";
		GameRegistry.registerBlock(this, MosfetItem.class, "Mosfet");
	}

	@Override
	public int getRenderType() {
		return renderid;
	}



	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}



	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TEMosfet();
	}

	@Override
	protected SimpleNode newNode() {
		return new MosfetNode();
	}

	@Override
	public AxisAlignedBB[] getSelectedBoundingBoxFromPool(World world, int x, int y, int z, EntityPlayer player) {
		return box;
	}
	
	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB ref, List list, Entity player) {
			setBlockBounds(5/16f, 3/16f, 5/16f, 11/16f, 11/16f, 11/16f);
			super.addCollisionBoxesToList(w, x, y, z, ref, list, player);
			setBlockBounds(0, 0, 0, 1, 2/16f, 1);
			super.addCollisionBoxesToList(w, x, y, z, ref, list, player);
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World wrd, int x, int y, int z, Vec3 origin, Vec3 direction) {
			MovingObjectPosition closest = null;
			for (AxisAlignedBB aabb : box) {
				MovingObjectPosition mop = aabb.getOffsetBoundingBox(x, y, z).calculateIntercept(origin, direction);
				if (mop != null) {
					if (closest != null && mop.hitVec.distanceTo(origin) < closest.hitVec.distanceTo(origin)) {
						closest = mop;
					} else {
						closest = mop;
					}
				}
			}
			if (closest != null) {
				closest.blockX = x;
				closest.blockY = y;
				closest.blockZ = z;
			}
			return closest==null?super.collisionRayTrace(wrd, x, y, z, origin, direction):closest;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
		int whichDirectionFacing = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		TileEntity te = par1World.getTileEntity(x, y, z);
		if(te instanceof TEMosfet){
			((TEMosfet) te).setFace(whichDirectionFacing);
		}
	}
	
}

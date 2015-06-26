package com.bymarcin.zettaindustries.mods.ocwires.block;

import java.util.ArrayList;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlockContainer;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockTelecomunicationConnector extends BasicBlockContainer {
	private static int renderId = RenderingRegistry.getNextAvailableRenderId();

	public BlockTelecomunicationConnector() {
		super(Material.iron, "TelecommunicationConnector");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTelecomunicationConnector();
	}

	@Override
	protected String getTextureName() {
		return ZettaIndustries.MODID + ":ocwires/metal_connectorTC";
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return false;
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
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityTelecomunicationConnector) {
			float length = .5f;
			switch (((TileEntityTelecomunicationConnector) world.getTileEntity(x, y, z)).getFacing())
			{
			case 0:// UP
				this.setBlockBounds(.3125f, 0, .3125f, .6875f, length, .6875f);
				break;
			case 1:// DOWN
				this.setBlockBounds(.3125f, 1 - length, .3125f, .6875f, 1, .6875f);
				break;
			case 2:// SOUTH
				this.setBlockBounds(.3125f, .3125f, 0, .6875f, .6875f, length);
				break;
			case 3:// NORTH
				this.setBlockBounds(.3125f, .3125f, 1 - length, .6875f, .6875f, 1);
				break;
			case 4:// EAST
				this.setBlockBounds(0, .3125f, .3125f, length, .6875f, .6875f);
				break;
			case 5:// WEST
				this.setBlockBounds(1 - length, .3125f, .3125f, 1, .6875f, .6875f);
				break;
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid)
	{
		if (world.getTileEntity(x, y, z) instanceof TileEntityTelecomunicationConnector)
		{
			TileEntityTelecomunicationConnector relay = (TileEntityTelecomunicationConnector) world.getTileEntity(x, y, z);
			ForgeDirection fd = ForgeDirection.getOrientation(relay.getFacing());
			if (world.isAirBlock(x + fd.offsetX, y + fd.offsetY, z + fd.offsetZ))
			{
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			}
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
	{
		if (world.getTileEntity(x, y, z) instanceof TileEntityTelecomunicationConnector)
		{
			ItemStack stack = new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
			return stack;
		}
		return super.getPickBlock(target, world, x, y, z, player);
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityTelecomunicationConnector && player != null && !player.capabilities.isCreativeMode)
		{
			ItemStack stack = new ItemStack(this, 1, meta);
			world.spawnEntityInWorld(new EntityItem(world, x + .5, y + .5, z + .5, stack));
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
		return ret;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

}

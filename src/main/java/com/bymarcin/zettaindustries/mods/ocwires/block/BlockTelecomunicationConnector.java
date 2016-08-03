package com.bymarcin.zettaindustries.mods.ocwires.block;

import blusunrize.immersiveengineering.api.IEProperties;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlockContainer;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTelecomunicationConnector extends BasicBlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockTelecomunicationConnector() {
		super(Material.IRON, "telecommunicationconnector");
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTelecomunicationConnector();
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}


	//	@Override
//	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
//	{
//		TileEntity te = world.getTileEntity(x, y, z);
//		if (te instanceof TileEntityTelecomunicationConnector) {
//			float length = .5f;
//			switch (((TileEntityTelecomunicationConnector) world.getTileEntity(x, y, z)).getFacing())
//			{
//			case 0:// UP
//				this.setBlockBounds(.3125f, 0, .3125f, .6875f, length, .6875f);
//				break;
//			case 1:// DOWN
//				this.setBlockBounds(.3125f, 1 - length, .3125f, .6875f, 1, .6875f);
//				break;
//			case 2:// SOUTH
//				this.setBlockBounds(.3125f, .3125f, 0, .6875f, .6875f, length);
//				break;
//			case 3:// NORTH
//				this.setBlockBounds(.3125f, .3125f, 1 - length, .6875f, .6875f, 1);
//				break;
//			case 4:// EAST
//				this.setBlockBounds(0, .3125f, .3125f, length, .6875f, .6875f);
//				break;
//			case 5:// WEST
//				this.setBlockBounds(1 - length, .3125f, .3125f, 1, .6875f, .6875f);
//				break;
//			}
//		}
//	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if (world.getTileEntity(pos) instanceof TileEntityTelecomunicationConnector)
		{
			TileEntityTelecomunicationConnector relay = (TileEntityTelecomunicationConnector) world.getTileEntity(pos);
			EnumFacing fd = EnumFacing.values()[relay.getFacing()];
			if (world.isAirBlock(neighbor.add(fd.getDirectionVec())))
			{
				dropBlockAsItem((World) world, pos, world.getBlockState(pos), 0);

				((World) world).setBlockToAir(pos);
			}
		}
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState ret = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		return ret.withProperty(FACING, facing.getOpposite());
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

}

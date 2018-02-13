package com.bymarcin.zettaindustries.mods.quarryfixer;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fluids.Fluid;

import buildcraft.builders.tile.TileQuarry;
import buildcraft.lib.misc.BlockUtil;
import buildcraft.lib.misc.data.Box;
import com.bymarcin.zettaindustries.basic.BasicBlock;

public class QuarryFixerBlock extends BasicBlock {
	public static final PropertyDirection front = PropertyDirection.create("front", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyInteger OnOff = PropertyInteger.create("on_off", 0, 1);
	public static final int ON = 1;
	public static final int OFF = 0;
	
	QuarryFixerBlock() {
		super(Material.IRON, "quarryfixer");
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(OnOff, meta & 1).withProperty(front, EnumFacing.Plane.HORIZONTAL.facings()[((meta >> 1) & 3)]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(OnOff) | state.getValue(front).ordinal() << 1;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, OnOff, front);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(front, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public int tickRate(World worldIn) {
		return 20;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		worldIn.setBlockState(pos, state.withProperty(OnOff, OFF), 2);
		super.updateTick(worldIn, pos, state, rand);
	}
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (state.getValue(OnOff) == ON) {
			return true;
		}
		TileQuarry quarry = null;
		for (EnumFacing dir : EnumFacing.HORIZONTALS) {
			BlockPos quarryPos = pos.offset(dir);
			if (worldIn.getTileEntity(quarryPos) instanceof TileQuarry) {
				quarry = (TileQuarry) worldIn.getTileEntity(quarryPos);
				break;
			}
		}
		if (quarry == null) {
			return false;
		}
		worldIn.setBlockState(pos, state.withProperty(OnOff, ON));
		worldIn.scheduleBlockUpdate(pos, state.getBlock(), tickRate(worldIn), 2);
		Box box = quarry.frameBox;
		for (int x = box.min().getX(); x <= box.max().getX(); x++) {
			for (int y = box.min().getY() - 1; y >= 1; y--) {
				for (int z = box.min().getZ(); z <= box.max().getZ(); z++) {
					BlockPos checkPos = new BlockPos(x, y, z);
					Fluid fluid = BlockUtil.getFluidWithFlowing(worldIn, checkPos);
					if (fluid == null) {
						continue;
					}
					if (x == box.min().getX() || x == box.max().getX() || z == box.min().getZ() || z == box.max().getZ()) {
						worldIn.setBlockState(checkPos, Blocks.STONE.getDefaultState(), 2);
					} else {
						worldIn.setBlockToAir(checkPos);
					}
				}
			}
		}
		return true;
	}
	
}

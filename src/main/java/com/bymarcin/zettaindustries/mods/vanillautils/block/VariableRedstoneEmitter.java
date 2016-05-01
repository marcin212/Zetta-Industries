package com.bymarcin.zettaindustries.mods.vanillautils.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.basic.BasicBlock;

public class VariableRedstoneEmitter extends BasicBlock {
	PropertyInteger STRENGTH = PropertyInteger.create("strength", 0, 15);

	public VariableRedstoneEmitter() {
		super(Material.IRON, "variableredstoneemitter");
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (player.getHeldItemMainhand() == null) {
			if (player.isSneaking()) {
				if (state.getValue(STRENGTH) > 0) {
					world.setBlockState(pos, state.withProperty(STRENGTH, state.getValue(STRENGTH) - 1), 2);
					world.notifyNeighborsOfStateChange(pos, state.getBlock());
					return true;
				}

				if (state.getValue(STRENGTH) == 0) {
					world.setBlockState(pos, state.withProperty(STRENGTH, 15), 2);
					world.notifyNeighborsOfStateChange(pos, state.getBlock());
					return true;
				}
			} else {
				if (state.getValue(STRENGTH) < 15) {
					world.setBlockState(pos, state.withProperty(STRENGTH, state.getValue(STRENGTH) + 1), 2);
					world.notifyNeighborsOfStateChange(pos, state.getBlock());
					return true;
				}

				if (state.getValue(STRENGTH) == 15) {
					world.setBlockState(pos, state.withProperty(STRENGTH, 0), 2);
					world.notifyNeighborsOfStateChange(pos, state.getBlock());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side != EnumFacing.UP && side != EnumFacing.DOWN;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return side != EnumFacing.UP && side != EnumFacing.DOWN ? blockState.getValue(STRENGTH) : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(STRENGTH, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(STRENGTH);
	}

	@Override
	public BlockStateContainer createBlockState() {
		STRENGTH = PropertyInteger.create("strength", 0, 15);
		return new BlockStateContainer(this, STRENGTH);
	}

}

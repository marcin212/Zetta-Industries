package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.basic.BasicBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BlockSulfur extends BasicBlock {
    private Block fluid;
    private List<String> info = new ArrayList<String>();

    public BlockSulfur(Block fluid) {
        super(Material.ROCK, "sulfurblock");
        setTickRandomly(true);
        this.fluid = fluid;
        info.add(localize("tooltip.sulfur1"));
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, this.tickRate(world), 0);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.getBlockState(pos.up()).getBlock() == Blocks.FIRE) {
            for (int i = pos.getX() - 1; i <= pos.getX() + 1; i++) {
                for (int j = pos.getZ() - 1; j <= pos.getZ() + 1; j++) {
                    Fluid f = FluidRegistry.lookupFluidForBlock(world.getBlockState(new BlockPos(i, pos.getY(), j)).getBlock());
                    if (FluidRegistry.getFluid("water").equals(f)) {
                        world.setBlockState(new BlockPos(i, pos.getY(), j), fluid.getDefaultState(), 2);
                    }
                }
            }
            world.setBlockToAir(pos);
        } else {
            world.scheduleBlockUpdate(pos, this, this.tickRate(world), 0);
        }
    }

    @Override
    public int tickRate(World par1World) {
        return 20 * 15;
    }

    @Override
    public List<String> getInformation() {
        return info;
    }
}

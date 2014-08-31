package com.bymarcin.zettaindustries.mods.battery.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlock;


public class BlockSulfur extends BasicBlock {
	protected IIcon blockIconTop;

	public BlockSulfur() {
		super(Material.rock, "sulfurblock");
		setTickRandomly(true);
	}

	
	@Override
	public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
		return side == ForgeDirection.UP;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.getBlock(x, y + 1, z) == Blocks.fire) {
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = z - 1; j <= z + 1; j++) {
					Fluid f = FluidRegistry.lookupFluidForBlock(world.getBlock(i, y, j));
					int fid = f != null ? f.getID() : -1;
					if (FluidRegistry.getFluid("water").getID() == fid && world.getBlockMetadata(i, y, j) == 0) {
						world.setBlock(i, y, j, this, 0, 2);
					}
				}
			}
			world.setBlockToAir(x, y, z);
		} else {
			world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/sulfur_block");
		blockIconTop = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/sulfur_block_top");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? blockIconTop : blockIcon;
	}

	@Override
	public int tickRate(World par1World) {
		return 20 * 15;
	}
}

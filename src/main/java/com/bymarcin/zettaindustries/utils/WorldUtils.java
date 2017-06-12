package com.bymarcin.zettaindustries.utils;

import com.bymarcin.zettaindustries.ZettaIndustries;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class WorldUtils {

	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
		World world = ZettaIndustries.proxy.getWorld(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(new BlockPos(x, y, z));
	}

	public static void dropItem(ItemStack item, Random rand, int x, int y, int z, World w) {
		if (!item.isEmpty()) {
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityItem = new EntityItem(w,
					x + rx, y + ry, z + rz,
					new ItemStack(item.getItem(), item.getCount(), item.getItemDamage()));
			if (item.hasTagCompound()) {
				entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
			}
			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			w.spawnEntity(entityItem);
			item.setCount(0);
		}
	}

	public static TileEntity getTileEntityServer(int dimensionId, int x, int y, int z) {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(new BlockPos(x, y, z));
	}

	public static boolean isClientWorld(World paramWorld) {
		return paramWorld.isRemote;
	}

	public static boolean isServerWorld(World paramWorld) {
		return !paramWorld.isRemote;
	}

	public static TileEntity getAdjacentTileEntity(World paramWorld, BlockPos pos, EnumFacing paramDirection) {
		return paramWorld == null ? null : paramWorld.getTileEntity(pos.add(paramDirection.getDirectionVec()));
	}

	public static IEnergyStorage getEnergyStorage(World world, BlockPos pos, EnumFacing facing) {
		TileEntity tileEntity = world.getTileEntity(pos.add(facing.getDirectionVec()));
		if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing)) {
			return tileEntity.getCapability(CapabilityEnergy.ENERGY, facing);
		}
		return null;
	}
}

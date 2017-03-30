package com.bymarcin.zettaindustries.utils;

import java.util.Random;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

//import cofh.api.energy.IEnergyHandler;

import com.bymarcin.zettaindustries.ZettaIndustries;

public class WorldUtils {
//	public static final ForgeDirection[] flatDirections = new ForgeDirection[]{ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST};

	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
		World world = ZettaIndustries.proxy.getWorld(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(new BlockPos(x, y, z));
	}
	
    public static void dropItem(ItemStack item, Random rand, int x, int y, int z, World w) {
        if (item != null && item.stackSize > 0) {
            float rx = rand.nextFloat() * 0.8F + 0.1F;
            float ry = rand.nextFloat() * 0.8F + 0.1F;
            float rz = rand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityItem = new EntityItem(w,
                    x + rx, y + ry, z + rz,
                    new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));
            if (item.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
            }
            float factor = 0.05F;
            entityItem.motionX = rand.nextGaussian() * factor;
            entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
            entityItem.motionZ = rand.nextGaussian() * factor;
            w.spawnEntityInWorld(entityItem);
            item.stackSize = 0;
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

	public static TileEntity getAdjacentTileEntity(World paramWorld, BlockPos pos, EnumFacing paramDirection)
	{
		return paramWorld == null ? null : paramWorld.getTileEntity(pos.add(paramDirection.getDirectionVec()));
	}

	public static IEnergyStorage getEnergyStorage(World world, BlockPos pos, EnumFacing facing){
		TileEntity tileEntity = world.getTileEntity(pos.add(facing.getDirectionVec()));
		if (tileEntity!=null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing)){
			return tileEntity.getCapability(CapabilityEnergy.ENERGY, facing);
		}
		return null;
	}

//	public static TileEntity getAdjacentTileEntity(World paramWorld, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
//	{
//	//	return paramWorld == null ? null : getAdjacentTileEntity(paramWorld, paramInt1, paramInt2, paramInt3, ForgeDirection.values()[paramInt4]);
//	}

//	public static TileEntity getAdjacentTileEntity(TileEntity paramTileEntity, ForgeDirection paramForgeDirection)
//	{
//		return paramTileEntity == null ? null : getAdjacentTileEntity(paramTileEntity.getWorldObj(), paramTileEntity.xCoord, paramTileEntity.yCoord, paramTileEntity.zCoord, paramForgeDirection);
//	}

//	public static TileEntity getAdjacentTileEntity(TileEntity paramTileEntity, int paramInt)
//	{
//		return paramTileEntity == null ? null : getAdjacentTileEntity(paramTileEntity.getWorldObj(), paramTileEntity.xCoord, paramTileEntity.yCoord, paramTileEntity.zCoord, ForgeDirection.values()[paramInt]);
//	}

	public static boolean isEnergyReciverFromSide(TileEntity paramTileEntity, EnumFacing paramDirection)
	{
		return (paramTileEntity instanceof IEnergyReceiver) && ((IEnergyReceiver) paramTileEntity).canConnectEnergy(paramDirection);
	}

//	public static Block getAdjencetBlock(TileEntity tile, ForgeDirection offset) {
//		return tile.getWorldObj().getBlock(tile.xCoord + offset.offsetX, tile.yCoord + offset.offsetY, tile.zCoord + offset.offsetZ);
//	}
}

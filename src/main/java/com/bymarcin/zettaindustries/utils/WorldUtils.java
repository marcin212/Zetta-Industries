package com.bymarcin.zettaindustries.utils;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

import com.bymarcin.zettaindustries.ZettaIndustries;

public class WorldUtils {
	public static final ForgeDirection[] flatDirections = new ForgeDirection[]{ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST};

	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
		World world = ZettaIndustries.proxy.getWorld(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(x, y, z);
	}

	public static TileEntity getTileEntityServer(int dimensionId, int x, int y, int z) {
		World world = MinecraftServer.getServer().worldServerForDimension(dimensionId);
		if (world == null)
			return null;
		return world.getTileEntity(x, y, z);
	}

	public static boolean isClientWorld(World paramWorld) {
		return paramWorld.isRemote;
	}

	public static boolean isServerWorld(World paramWorld) {
		return !paramWorld.isRemote;
	}

	public static TileEntity getAdjacentTileEntity(World paramWorld, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection)
	{
		return paramWorld == null ? null : paramWorld.getTileEntity(paramInt1 + paramForgeDirection.offsetX, paramInt2 + paramForgeDirection.offsetY, paramInt3 + paramForgeDirection.offsetZ);
	}

	public static TileEntity getAdjacentTileEntity(World paramWorld, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
		return paramWorld == null ? null : getAdjacentTileEntity(paramWorld, paramInt1, paramInt2, paramInt3, ForgeDirection.values()[paramInt4]);
	}

	public static TileEntity getAdjacentTileEntity(TileEntity paramTileEntity, ForgeDirection paramForgeDirection)
	{
		return paramTileEntity == null ? null : getAdjacentTileEntity(paramTileEntity.getWorldObj(), paramTileEntity.xCoord, paramTileEntity.yCoord, paramTileEntity.zCoord, paramForgeDirection);
	}

	public static TileEntity getAdjacentTileEntity(TileEntity paramTileEntity, int paramInt)
	{
		return paramTileEntity == null ? null : getAdjacentTileEntity(paramTileEntity.getWorldObj(), paramTileEntity.xCoord, paramTileEntity.yCoord, paramTileEntity.zCoord, ForgeDirection.values()[paramInt]);
	}

	public static boolean isEnergyHandlerFromSide(TileEntity paramTileEntity, ForgeDirection paramForgeDirection)
	{
		return (paramTileEntity instanceof IEnergyHandler) && ((IEnergyHandler) paramTileEntity).canConnectEnergy(paramForgeDirection);
	}

	public static Block getAdjencetBlock(TileEntity tile, ForgeDirection offset) {
		return tile.getWorldObj().getBlock(tile.xCoord + offset.offsetX, tile.yCoord + offset.offsetY, tile.zCoord + offset.offsetZ);
	}
}

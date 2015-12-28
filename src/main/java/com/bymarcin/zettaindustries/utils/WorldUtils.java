package com.bymarcin.zettaindustries.utils;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;

import com.bymarcin.zettaindustries.ZettaIndustries;

public class WorldUtils {
	public static final ForgeDirection[] flatDirections = new ForgeDirection[]{ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST};

	/*
		Original methods from ZIndustries modified to fit BuildCraft-style "forced chunk" methods
	*/
	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
		return this.getTileEntity(dimensionId, x, y, z, true);
	}
	
	public static TileEntity getTileEntityServer(int dimensionId, int x, int y, int z) {
		return this.getTileEntity(dimensionId, x, y, z, true);
	}
	
	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z, boolean force) {
		World world = ZettaIndustries.proxy.getWorld(dimensionId);
		return world == null ? null : this.getTileEntity(world, x, y, z, true);
	}
	
	public static TileEntity getTileEntityServer(int dimensionId, int x, int y, int z, boolean force) {
		World world = MinecraftServer.getServer().worldServerForDimension(dimensionId);
		return world == null ? null : this.getTileEntity(world, x, y, z, true);
	}
	
	/**
	 * Methods adapted from BuildCraft
	 */
	 
	private static TileEntity getTileEntity(World world, int x, int y, int z, boolean force) {
		if (!force) {
			if (y < 0 || y > 255) {
				return null;
			}
			Chunk chunk = getChunkUnforced(world, x >> 4, z >> 4);
			return chunk != null ? chunk.getTileEntityUnsafe(x & 15, y, z & 15) : null;
		} else return world.getTileEntity(x, y, z);
	}
	
	private static Chunk lastChunk;

	private static Chunk getChunkUnforced(World world, int x, int z) {
		Chunk chunk = lastChunk;
		if (chunk != null) {
			if (chunk.isChunkLoaded) {
				if (chunk.worldObj == world && chunk.xPosition == x && chunk.zPosition == z) {
					return chunk;
				}
			} else {
				lastChunk = null;
			}
		}

		chunk = world.getChunkProvider().chunkExists(x, z) ? world.getChunkProvider().provideChunk(x, z) : null;
		lastChunk = chunk;
		return chunk;
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

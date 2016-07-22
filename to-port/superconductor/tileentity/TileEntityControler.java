package com.bymarcin.zettaindustries.mods.superconductor.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.api.energy.IEnergyHandler;

import com.bymarcin.zettaindustries.mods.superconductor.SuperConductorMod;
import com.bymarcin.zettaindustries.mods.superconductor.block.BlockControler;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.utils.WorldUtils;

public class TileEntityControler extends TileEntityBase implements IEnergyHandler, IFluidHandler {
	ArrayList<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();

	public boolean isOutput() {
		return BlockControler.getBlocktype(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) == 0 ? true : false;
	}

	public ForgeDirection getFaceSide() {
		return ForgeDirection.getOrientation((worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7));
	}

	public int onReceiveEnergy(int maxReceive, boolean simulate) {
		TileEntity tile = WorldUtils.getAdjacentTileEntity(this, getFaceSide());
		return (isOutput() && WorldUtils.isEnergyHandlerFromSide(tile, getFaceSide().getOpposite())) ?
				((IEnergyHandler) tile).receiveEnergy(getFaceSide().getOpposite(), maxReceive, simulate) : 0;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7) == from.ordinal();
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return (canConnectEnergy(from) && !isOutput() && getMultiblockController()!=null) ? getMultiblockController().onReceiveEnergy(maxReceive, simulate) : 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 0;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid == SuperConductorMod.coolantFluid;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return getMultiblockController() != null ? getMultiblockController().onFillCoolant(resource) : 0;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return getMultiblockController() != null ? getMultiblockController().getCoolantTankInfo() : null;
	}

	public void updateGUI() {
		for (EntityPlayerMP p : players)
			ZIRegistry.packetHandler.sendTo(getMultiblockController().getUpdatePacket(this), p);
	}

	public void addPlayerToUpdate(EntityPlayerMP p) {
		players.add(p);
		if (WorldUtils.isServerWorld(worldObj))
			ZIRegistry.packetHandler.sendTo(getMultiblockController().getUpdatePacket(this), p);
	}

	public void removePlayerFromUpdate(EntityPlayer p) {
		players.remove(p);
	}

	// Ignored ||
	//         \/
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}
}

package com.bymarcin.zettaindustries.mods.superconductor.gui;

import java.io.IOException;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityControler;
import com.bymarcin.zettaindustries.registry.network.Packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketUpdateFluidAmount extends Packet<PacketUpdateFluidAmount, IMessage>{
	int fluidAmount;
	int fluidID;
	TileEntity tile;
	public PacketUpdateFluidAmount() {
	}
	
	public PacketUpdateFluidAmount(TileEntity tile, int fluidAmount, int fluidID) {
		this.fluidAmount = fluidAmount;
		this.fluidID = fluidID;
		this.tile = tile;
	}

	@Override
	protected void read() throws IOException {
		tile = readClientTileEntity();
		fluidAmount = readInt();
		fluidID = readInt();	
	}

	@Override
	protected void write() throws IOException {
		writeTileLocation(tile);
		writeInt(fluidAmount);
		writeInt(fluidID);
		
	}

	@Override
	protected IMessage executeOnClient() {
		TileEntityControler te= (TileEntityControler) tile;
		FluidStack fluid = new FluidStack(fluidID, fluidAmount);
		if(te!=null && te.getMultiblockController() !=null){
			((SuperConductor)te.getMultiblockController()).getTank().setFluid(fluid);
		}
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}

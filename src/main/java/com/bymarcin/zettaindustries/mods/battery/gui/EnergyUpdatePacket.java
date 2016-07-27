package com.bymarcin.zettaindustries.mods.battery.gui;

import java.io.IOException;

import com.bymarcin.zettaindustries.registry.network.Packet;
import net.minecraft.tileentity.TileEntity;

import com.bymarcin.zettaindustries.mods.battery.tileentity.BatteryController;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityControler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class EnergyUpdatePacket extends Packet<EnergyUpdatePacket,IMessage> {
	long capacity;
	long energy;
	TileEntity te;
	
	public EnergyUpdatePacket() {
		
	}
	
	public EnergyUpdatePacket(TileEntity tile, long energy, long capacity) {
		this.capacity = capacity;
		this.energy = energy;
		this.te = tile;
	}
	
	
	@Override
	public void write() throws IOException{
		writeTileLocation(te);
		writeLong(capacity);
		writeLong(energy);
		
	}

	@Override
	public void read() throws IOException{
		te = readClientTileEntity();
		capacity = readLong();
		energy = readLong();
		
	}


	@Override
	protected IMessage executeOnClient() {
		if(te instanceof TileEntityControler){
			((BatteryController) ((TileEntityControler) te).getMultiblockController()  ).onPacket(capacity,energy);
		}
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}

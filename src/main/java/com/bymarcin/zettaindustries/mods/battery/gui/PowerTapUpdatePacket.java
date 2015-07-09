package com.bymarcin.zettaindustries.mods.battery.gui;

import java.io.IOException;

import net.minecraft.tileentity.TileEntity;

import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityPowerTap;
import com.bymarcin.zettaindustries.registry.network.Packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PowerTapUpdatePacket extends Packet<PowerTapUpdatePacket,IMessage>{
	int transfer = 0;
	TileEntity te;
	boolean side;
	String label;
	
	public PowerTapUpdatePacket(TileEntity te, int transfer, boolean side, String label) {
		this.transfer  = transfer; 
		this.te =te;
		this.side = side;
		this.label = label;
	}
	
	public PowerTapUpdatePacket() {
	}

	@Override
	public void write() throws IOException{
		writeBoolean(side);
		writeTileLocation(te);
		writeInt(transfer);	
		writeString(label);
	}

	@Override
	public void read() throws IOException {
		if(readBoolean())
			te = readClientTileEntity();
		else
			te = readServerTileEntity();
		transfer = readInt();
		label = readString();
	}

	@Override
	protected IMessage executeOnClient() {
		if(te!=null){
			((TileEntityPowerTap) te).setTransfer(transfer);
			((TileEntityPowerTap) te).setLabel(label);
		}
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		if(te!=null) {
            ((TileEntityPowerTap) te).setTransfer(transfer);
            ((TileEntityPowerTap) te).setLabel(label);
            ((TileEntityPowerTap) te).updatePowerTap();
        }
		return null;
	}
}

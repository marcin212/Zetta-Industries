package com.bymarcin.zettaindustries.mods.rfpowermeter;

import java.io.IOException;

import com.bymarcin.zettaindustries.registry.network.Packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class RFMeterUpdatePacket extends Packet<RFMeterUpdatePacket,IMessage>{
	long value;
	int transfer;
	RFMeterTileEntity te;
	boolean inCounterMode;
	
	public RFMeterUpdatePacket() {
	}
	
	public RFMeterUpdatePacket(RFMeterTileEntity te, long value, int transfer, boolean inCounterMode) {
		this.te = te;
		this.value = value;
		this.transfer = transfer;
		this.inCounterMode = inCounterMode;
	}

	@Override
	protected void read() throws IOException {
		te  = (RFMeterTileEntity) readClientTileEntity();
		value = readLong();
		transfer = readInt();
		inCounterMode = readBoolean();
	}

	@Override
	protected void write() throws IOException {
		writeTileLocation(te);
		writeLong(value);
		writeInt(transfer);
		writeBoolean(inCounterMode);
	}

	@Override
	protected IMessage executeOnClient() {
		if(te==null) return null;
		te.onPacket(value, transfer, inCounterMode);
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		return null;
	}

}

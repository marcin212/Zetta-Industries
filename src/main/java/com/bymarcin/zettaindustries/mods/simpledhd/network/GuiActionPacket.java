package com.bymarcin.zettaindustries.mods.simpledhd.network;

import java.io.IOException;

import com.bymarcin.zettaindustries.mods.simpledhd.tileentity.TileEntitySimpleDHD;
import com.bymarcin.zettaindustries.registry.network.Packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class GuiActionPacket extends Packet<GuiActionPacket, IMessage> {
	boolean action;
	TileEntitySimpleDHD entity;
	String address;
	public GuiActionPacket(){
		
	}
	
	public GuiActionPacket(TileEntitySimpleDHD entity, String address, boolean action) {
		this.action = action;
		this.entity = entity;
		this.address = address;
	}

	@Override
	protected void read() throws IOException {
		action = readBoolean();
		if(action){
			address = readString();
		}
		try {
			this.entity = (TileEntitySimpleDHD) readServerTileEntity();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ClassCastException ex){
			ex.printStackTrace();
		}
	}

	@Override
	protected void write() throws IOException {
		writeBoolean(action);
		if(action){
			writeString(address);
		}
		writeTileLocation(entity);
	}

	@Override
	protected IMessage executeOnClient() {
		return null;
	}

	@Override
	protected IMessage executeOnServer() {
		if(entity!=null){
			if(action){
				entity.setAddress(address);
				entity.dial();
			}else{
				entity.disconnect();
			}
		}
		return null;
	}

}

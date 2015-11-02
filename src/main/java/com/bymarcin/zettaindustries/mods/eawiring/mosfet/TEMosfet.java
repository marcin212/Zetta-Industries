package com.bymarcin.zettaindustries.mods.eawiring.mosfet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.primitives.Ints;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

import lordfokas.stargatetech2.core.ByteUtil;
import mods.eln.Eln;
import mods.eln.misc.Direction;
import mods.eln.misc.Utils;
import mods.eln.node.simple.SimpleNode;
import mods.eln.node.simple.SimpleNodeEntity;

public class TEMosfet extends SimpleNodeEntity{

	int face;
	
	@Override
	public String getNodeUuid() {
		return MosfetNode.getNodeUuidStatic();
	}
	
	
	public void setFace(int face) {
		this.face = face;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("face", face);
		super.writeToNBT(nbt);
	}
	
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
    	face = nbt.getInteger("face");
    	super.readFromNBT(nbt);
    }
    
    
	public Direction front;
	@Override
	public void serverPublishUnserialize(DataInputStream stream) {
		try {
			if(front != (front = Direction.fromInt(stream.readByte()))){
				worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
			}
			if(stream.available()>3){
				face = stream.readInt();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void serverPacketUnserialize(DataInputStream stream) {

	}
	
    @Override
    public Packet getDescriptionPacket()
    {	
    	SimpleNode node = getNode(); 
    	if(node == null){
    		Utils.println("ASSERT NULL NODE public Packet getDescriptionPacket() nodeblock entity");
    		return null;
    	}
    	
    	ByteArrayOutputStream out = node.getPublishPacket();
    	try {
			out.write(Ints.toByteArray(face));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return new S3FPacketCustomPayload(Eln.channelName,out.toByteArray());
    }
    
    
}

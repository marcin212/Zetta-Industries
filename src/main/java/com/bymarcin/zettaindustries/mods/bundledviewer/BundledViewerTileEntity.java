package com.bymarcin.zettaindustries.mods.bundledviewer;

import org.apache.commons.lang3.ArrayUtils;

import mods.immibis.redlogic.api.wiring.IBundledEmitter;
import mods.immibis.redlogic.api.wiring.IBundledUpdatable;
import mods.immibis.redlogic.api.wiring.IBundledWire;
import mods.immibis.redlogic.api.wiring.IConnectable;
import mods.immibis.redlogic.api.wiring.IWire;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.util.ForgeDirection;

public class BundledViewerTileEntity extends TileEntity implements IConnectable, IBundledUpdatable{
	byte[] signals = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	
	public byte[] getSignals(){
		return signals;
	}
	
	
	@Override
	public void onBundledInputChanged() {
		signals = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity te = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord +  dir.offsetZ);
			if (te instanceof IBundledEmitter) {
				IBundledEmitter emitter = (IBundledEmitter) te;
				for(int i = -1; i<=5;i++){
					byte[] newState = emitter.getBundledCableStrength(i, dir.getOpposite().ordinal());
					if(newState!=null)
						for(int c=0; c<newState.length;c++){
							signals[c] |= newState[c];
						}
				}
			}
			
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			worldObj.notifyBlockChange(xCoord, yCoord, zCoord, blockType);
			markDirty();
		}
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		signals = pkt.func_148857_g().getByteArray("SIGNALS");
		if(signals.length!=16){
			signals = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, tag);
	}

	@Override
	public boolean connects(IWire wire, int blockFace, int fromDirection) {
		return true;
	}

	@Override
	public boolean connectsAroundCorner(IWire wire, int blockFace, int fromDirection) {
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setByteArray("SIGNALS", signals);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		signals = tag.getByteArray("SIGNALS");
		if(signals.length!=16){
			signals = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		}
	}

}

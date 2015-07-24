package com.bymarcin.zettaindustries.mods.simpledhd.tileentity;

import java.util.LinkedList;

import com.bymarcin.zettaindustries.ZettaIndustries;

import lordfokas.stargatetech2.api.StargateTechAPI;
import lordfokas.stargatetech2.api.bus.BusPacket;
import lordfokas.stargatetech2.api.bus.BusPacketLIP;
import lordfokas.stargatetech2.api.bus.IBusDevice;
import lordfokas.stargatetech2.api.bus.IBusDriver;
import lordfokas.stargatetech2.api.bus.IBusInterface;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySimpleDHD extends TileEntity implements IBusDevice, IBusDriver {
	String address = "";
	IBusInterface businterface = StargateTechAPI.api().getFactory().getIBusInterface(this, this);
	int face;

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("address", address);
		businterface.writeToNBT(nbt, "interface");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("address"))
			address = nbt.getString("address");
		businterface.readFromNBT(nbt, "interface");
	}

	public void dial() {
		BusPacketLIP packet = new BusPacketLIP((short) 0, (short) 255);
		packet.set("action", "dial");
		packet.set("address", getAddress());
		packet.setMetadata(new BusPacketLIP.LIPMetadata(ZettaIndustries.MODID, getShortName(), null));
		packet.finish();
		handlePacket(packet);
		businterface.sendAllPackets();
	}

	public void disconnect() {
		BusPacketLIP packet = new BusPacketLIP((short) 0, (short) 255);
		packet.set("action", "disconnect");
		packet.setMetadata(new BusPacketLIP.LIPMetadata(ZettaIndustries.MODID, getShortName(), null));
		packet.finish();
		handlePacket(packet);
		businterface.sendAllPackets();
	}

	@Override
	public IBusInterface[] getInterfaces(int side) {
		if (ForgeDirection.DOWN.ordinal() == side) {
			return new IBusInterface[] { businterface };
		} else {
			return new IBusInterface[] {};
		}
	}

	@Override
	public World getWorld() {
		return worldObj;
	}

	@Override
	public int getXCoord() {
		return xCoord;
	}

	@Override
	public int getYCoord() {
		return yCoord;
	}

	@Override
	public int getZCoord() {
		return zCoord;
	}

	private LinkedList<BusPacket> queue = new LinkedList<BusPacket>();

	@Override
	public boolean canHandlePacket(short arg0, int arg1, boolean arg2) {
		return false;
	}

	@Override
	public String getDescription() {
		return "SimpleDHD";
	}

	@Override
	public short getInterfaceAddress() {
		return 0x00;
	}

	@Override
	public BusPacket getNextPacketToSend() {
		return queue.isEmpty() ? null : queue.removeFirst();
	}

	@Override
	public String getShortName() {
		return "SDHD";
	}

	@Override
	public void handlePacket(BusPacket packet) {
		queue.addLast(packet);
	}

	@Override
	public boolean isInterfaceEnabled() {
		return true;
	}

}

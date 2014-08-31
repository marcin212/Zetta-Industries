package com.bymarcin.zettaindustries.mods.powermeter;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.core.IIconProvider;
import buildcraft.transport.IPipeTransportPowerHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;

public class PowerMeterPipe extends Pipe<PipeTransportPower> implements IPipeTransportPowerHook {

	double energyUsed = 0;
	double avg = 0;
	boolean inCounterMode = true;
	boolean isOn = true;
	String password = "";
	boolean isProtected = false;
	String name;
	ExtendedAverage a = new ExtendedAverage(200);
	long tick = 0;
	
	public PowerMeterPipe(Item item) {
		super(new PipeTransportPower(), item);
		PipeTransportPower.powerCapacities.put(PowerMeterPipe.class, 1024);
		transport.initFromPipe(getClass());
	}

	public boolean canEdit(String pass) {
		return !isProtected || (isProtected && pass.equals(password));
	}

	public double getAvg() {
		return avg;
	}

	public String getName() {
		return name;
	}

	public void setState(boolean state) {
		isOn = state;
	}

	public boolean getState() {
		return isOn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean canFlowEnergy() {
		return isOn && (inCounterMode || (0 < energyUsed));
	}

	public void setPassword(String pass) {
		password = pass;
		isProtected = true;
	}

	public void removePassword() {
		password = "";
		isProtected = false;
	}

	public void setEnergyCounter(int val) {
		energyUsed = val;
	}

	public double getEnergyUsed() {
		return energyUsed;
	}
	
	public void setCounterMode(boolean mode) {
		inCounterMode = mode;
	}

	public boolean getCounterMode() {
		return inCounterMode;
	}

	@Override
	public double receiveEnergy(ForgeDirection from, double val) {
		if (!from.equals(ForgeDirection.DOWN) || !canFlowEnergy())
			return 0;
		if (val > 0.0) {
			val = inCounterMode ? val : Math.min(energyUsed, val);
			transport.internalNextPower[from.ordinal()] += val;
			if (transport.internalNextPower[from.ordinal()] > transport.maxPower) {
				val = transport.internalNextPower[from.ordinal()] - transport.maxPower;
				transport.internalNextPower[from.ordinal()] = transport.maxPower;
			}
		}
		if (inCounterMode)
			energyUsed += val;
		else
			energyUsed -= val;
		
		a.add(val);
		
		if(tick%200==0)
			System.out.println("ASD: " + a.get());
		
		tick++;
		avg = (avg * 39.0 + (double) val) / 40.0;
		return val;
	}

	@Override
	public double requestEnergy(ForgeDirection from, double amount) {
		if (from.equals(ForgeDirection.UP) && canFlowEnergy())
			return amount;
		return 0;
	}

	@Override
	public int getIconIndex(ForgeDirection arg0) {
		return 0;
	}

	@Override
	public int getIconIndexForItem() {
		return 0;
	}

	@Override
	public IIconProvider getIconProvider() {
		return PowerMeter.iconProvider;
	}

	@Override
	public boolean canPipeConnect(TileEntity tile, ForgeDirection side) {
		return super.canPipeConnect(tile, side) && (side.equals(ForgeDirection.UP) || side.equals(ForgeDirection.DOWN));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("inCounterMode", inCounterMode);
		nbt.setBoolean("isOn", isOn);
		nbt.setBoolean("isProtected", isProtected);

		nbt.setDouble("energyUsed", energyUsed);
		nbt.setDouble("avg", avg);

		if(password !=null && !password.isEmpty())
			nbt.setString("password", password);
		if(name !=null && !name.isEmpty())
			nbt.setString("nameP", name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inCounterMode = nbt.getBoolean("inCounterMode");
		isOn = nbt.getBoolean("isOn");
		isProtected = nbt.getBoolean("isProtected");

		energyUsed = nbt.getDouble("energyUsed");
		avg = nbt.getDouble("avg");
		
		if(nbt.hasKey("password"))
			password = nbt.getString("password");
		else
			password="";
		
		if(nbt.hasKey("nameP"))
			name = nbt.getString("nameP");
		else
			name="";
	}

}

package com.bymarcin.zettaindustries.mods.eawiring.connection;

import mods.eln.Eln;

import net.minecraft.nbt.NBTTagCompound;

public class WireProperties implements Cloneable{
    public double thermalCoolLimit = -100;
    public double thermalNominalHeatTime = Eln.cableHeatingTime;
    public double thermalConductivityTao = Eln.cableThermalConductionTao;
    
    public double electricalMaximalV;
    public double thermalWarmLimit = Eln.cableWarmLimit;
    public double resistance;
    public double electricalMaximalI;
    public double thermalMaximalPowerDissipated;
    public double distance;
    
    public WireProperties() {
	}
    
 
	public WireProperties(double thermalWarmLimit, double electricalMaximalI, double electricalMaximalV, double resistance) {
		this.thermalWarmLimit = thermalWarmLimit;
		this.resistance = resistance;
		this.electricalMaximalI = electricalMaximalI;
		thermalMaximalPowerDissipated = electricalMaximalI * electricalMaximalI * resistance * 2;
		this.electricalMaximalV = electricalMaximalV;
	}


	public void readFromNBT(NBTTagCompound nbt, String str) {
	    thermalWarmLimit = nbt.getDouble(str+".thermalWarmLimit");
	    resistance = nbt.getDouble(str+".resistance");
	    electricalMaximalI = nbt.getDouble(str+".electricalMaximalI");
	    thermalMaximalPowerDissipated = nbt.getDouble(str+".thermalMaximalPowerDissipated");
	    distance = nbt.getDouble(str+".distance");
	    electricalMaximalV =  nbt.getDouble(str+".electricalMaximalV");
	    
	}


	public void writeToNBT(NBTTagCompound nbt, String str) {
		nbt.setDouble(str+".thermalWarmLimit", thermalWarmLimit);
		nbt.setDouble(str+".resistance", resistance);
		nbt.setDouble(str+".electricalMaximalI", electricalMaximalI);
		nbt.setDouble(str+".thermalMaximalPowerDissipated", thermalMaximalPowerDissipated);
		nbt.setDouble(str+".distance", distance);
		nbt.setDouble(str+".electricalMaximalV", electricalMaximalV);
	}

	public void setDistance(double distance){
		this.distance = distance;
		this.thermalMaximalPowerDissipated = this.electricalMaximalI * this.electricalMaximalI * (this.resistance*distance);
	}

	public double getResistancePerBlock() {
		return resistance;
	}


	public double getThermalC() {
		return thermalMaximalPowerDissipated * thermalNominalHeatTime / (thermalWarmLimit);
	}


	public double getThermalRp() {
		return thermalWarmLimit / thermalMaximalPowerDissipated;
	}

	public double getThermalRs() {
		return thermalConductivityTao / getThermalC() / 2;
	}
	
	@Override
	public Object clone() {
		return new WireProperties(thermalWarmLimit, electricalMaximalI, electricalMaximalV, resistance);
	}
}

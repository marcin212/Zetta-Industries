package com.bymarcin.zettaindustries.mods.eawiring.connectors.wires;

import com.bymarcin.zettaindustries.mods.eawiring.connection.WireProperties;

import net.minecraft.util.IIcon;

import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;

public abstract class WireBase extends WireType{
	WireProperties wireProperties;
	public static WireBase LVWire = new LVWire();
	public static WireBase MVWire = new MVWire();
	public static WireBase HVWire = new HVWire();
	
	public WireBase() {
		
	}
	
	public WireProperties getWireProperties(){
		return (WireProperties) wireProperties.clone();
	}
	
	@Override
	public IIcon getIcon(Connection arg0) {
		return iconDefaultWire;
	}

	@Override
	public double getLossRatio() {
		return 0;
	}

	@Override
	public double getSlack() {
		return 1.005;
	}

	@Override
	public int getTransferRate() {
		return 0;
	}

	@Override
	public boolean isEnergyWire() {
		return false;
	}
}

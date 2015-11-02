package com.bymarcin.zettaindustries.mods.eawiring.connection;

import mods.eln.sim.ElectricalConnection;
import mods.eln.sim.ElectricalLoad;

public class VirtualElectricalConnection extends ElectricalConnection{

	public VirtualElectricalConnection(ElectricalLoad L1, ElectricalLoad L2) {
		super(L1, L2);
	}
	
	@Override
	public void notifyRsChange() {

	}

}

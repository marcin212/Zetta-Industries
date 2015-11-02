package com.bymarcin.zettaindustries.mods.rfpowermeter;

public class IntegrationComputerCraft {
	
	public static void computercraftInit(){
		dan200.computercraft.api.ComputerCraftAPI.registerPeripheralProvider(new RFMeterTileEntityOC());
	}

}

package com.bymarcin.zettaindustries.mods.battery;

import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityComputerPort;

public class IntegrationComputerCraft {
	public static void computercraftInit(){
		dan200.computercraft.api.ComputerCraftAPI.registerPeripheralProvider(new TileEntityComputerPort());
	}
}

package com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks;

import forestry.api.storage.EnumBackpackType;

public class ImmersiveEngineeringBackpack extends BasicBackpack{

	public ImmersiveEngineeringBackpack(EnumBackpackType type) {
		super(type, "ImmersiveEngineering", 
					"immersiveintegration", 
					"zettaindustries:hook",
					"zettaindustries:Telecommunication",
					"zettaindustries:Lightingfirework", 
					"zettaindustries:BlockTelecommunicationConnector");
	}
	
	@Override
	public String getUniqueName() {
		return "backpack.engineering";
	}

	@Override
	public int getPrimaryColour() {
		return 0x452618;
	}

	@Override
	public int getSecondaryColour() {
		return 0xbf853e;
	}

}

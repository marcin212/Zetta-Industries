package com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks;

import forestry.api.storage.EnumBackpackType;

public class OCBackpack extends BasicBackpack{

	public OCBackpack(EnumBackpackType type){
		super(type, "OpenComputers",
					"Computronics",
					"OpenPrinter",
					"opensecurity",
					"masssound",
					"OpenGlasses",
					"zettaindustries:nfc",
					"zettaindustries:mailman",
					"zettaindustries:ecatalogue");
	}
	
	@Override
	public String getUniqueName() {
		return "backpack.opencomputers";
	}

	@Override
	public int getPrimaryColour() {
		return 0x4ea15c;
	}

	@Override
	public int getSecondaryColour() {
		return 0xdcdcdc;
	}

}

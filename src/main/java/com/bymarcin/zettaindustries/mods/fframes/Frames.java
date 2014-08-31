package com.bymarcin.zettaindustries.mods.fframes;

import com.bymarcin.zettaindustries.modmanager.IMod;

import cpw.mods.fml.common.registry.GameRegistry;

public class Frames implements IMod{

	@Override
	public void init() {
		GameRegistry.registerItem(new LarvaeFrame(), "larvaeFrame");
		
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
	}
	
}

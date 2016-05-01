package com.bymarcin.zettaindustries.basic;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.item.Item;

public class FakeItemIcon extends Item{
	
	public FakeItemIcon() {
		setMaxStackSize(1);
		setRegistryName("logo");
		setUnlocalizedName(ZettaIndustries.MODID + ".logo");
		ZettaIndustries.proxy.registermodel(this, 0);
		setCreativeTab(ZettaIndustries.tabZettaIndustries);
	}
	
}

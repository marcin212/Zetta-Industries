package com.bymarcin.zettaindustries.basic;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.item.Item;

public class BasicItem extends Item {
	public BasicItem(String name) {
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setMaxStackSize(64);
		setUnlocalizedName(name);
	}
}

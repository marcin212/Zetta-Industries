package com.bymarcin.zettaindustries;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ZettaIndustriesCreativeTab extends CreativeTabs{
	
	public ZettaIndustriesCreativeTab() {
		super(ZettaIndustries.MODID);
	}
	
	@Override
	public Item getTabIconItem() {
		return ZettaIndustries.itemLogo;
	}
}

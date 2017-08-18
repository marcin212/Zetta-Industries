package com.bymarcin.zettaindustries;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ZettaIndustriesCreativeTab extends CreativeTabs{
	
	public ZettaIndustriesCreativeTab() {
		super(ZettaIndustries.MODID);
	}
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ZettaIndustries.itemLogo);
	}
}

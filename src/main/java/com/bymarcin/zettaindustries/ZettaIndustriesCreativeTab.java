package com.bymarcin.zettaindustries;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.bymarcin.zettaindustries.basic.FakeItemIcon;

import cpw.mods.fml.common.registry.GameRegistry;

public class ZettaIndustriesCreativeTab extends CreativeTabs{
	
	static Item icon;
	
	public ZettaIndustriesCreativeTab() {
		super(ZettaIndustries.MODID);
		icon = new FakeItemIcon();
		GameRegistry.registerItem(icon, ZettaIndustries.MODID+".fakeitem");
	}
	
	@Override
	public Item getTabIconItem() {
		return icon;
	}
}

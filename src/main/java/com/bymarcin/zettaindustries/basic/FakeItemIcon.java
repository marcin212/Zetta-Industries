package com.bymarcin.zettaindustries.basic;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class FakeItemIcon extends Item{
	
	public FakeItemIcon() {
		setMaxStackSize(0);
	}
	
	@Override
	public void registerIcons(IIconRegister registry) {
		itemIcon = registry.registerIcon(ZettaIndustries.MODID + ":logo");
	}

	
}

package com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks;

import net.minecraft.item.ItemStack;

import forestry.api.storage.EnumBackpackType;

public class CreativeBackpack extends BasicBackpack{

	public CreativeBackpack(EnumBackpackType type) {
		super(type);
	}

	@Override
	public int getPrimaryColour() {
		return 0xc70e14;
	}

	@Override
	public int getSecondaryColour() {
		return 0xd4c9a9;
	}

	@Override
	public boolean test(ItemStack itemstack) {
		return true;
	}

	@Override
	public String getUniqueName() {
		return "backpack.creative";
	}
}

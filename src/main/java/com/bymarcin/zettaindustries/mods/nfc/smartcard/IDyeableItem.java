package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import net.minecraft.item.ItemStack;

public interface IDyeableItem {
	boolean hasColor(ItemStack stack);
	int getColor(ItemStack stack);
	void setColor(ItemStack stack, int color);
	boolean removeColor(ItemStack stack);
}

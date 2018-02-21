package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.mods.nfc.NFC;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class SmartCardItemColor implements IItemColor {
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if (tintIndex == 0) {
			return NFC.smartCardItem.hasColor(stack) ? NFC.smartCardItem.getColor(stack) : 0xFF316ac8;
		} else {
			return -1;
		}
	}
}

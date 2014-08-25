package com.bymarcin.zettaindustries.mods.vanillautils;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class CharcoalFuelHandler implements IFuelHandler{
	@Override
	public int getBurnTime(ItemStack fuel) {
		
		if (Block.getBlockFromItem(fuel.getItem()) == VanillaUtils.charcoalblock) {
			return 16000;
		}
		return 0;
	}
}

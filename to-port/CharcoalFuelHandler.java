package com.bymarcin.zettaindustries.mods.battery;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class CharcoalFuelHandler implements IFuelHandler {
	@Override
	public int getBurnTime(ItemStack fuel) {
		
		if (Block.getBlockFromItem(fuel.getItem()) == CharcoalBlockMod.charcoalblock) {
			return 16000;
		}
		return 0;
	}
}

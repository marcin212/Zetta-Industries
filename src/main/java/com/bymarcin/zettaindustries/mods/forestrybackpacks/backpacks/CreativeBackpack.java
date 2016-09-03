package com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks;

import net.minecraft.item.ItemStack;

import forestry.api.storage.EnumBackpackType;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CreativeBackpack extends BasicBackpack{
	True filter = new True();

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

	@Nonnull
	@Override
	public Predicate<ItemStack> getFilter() {
		return filter;
	}

	@Override
	public String getUniqueName() {
		return "backpack.creative";
	}

	public static class True implements Predicate<ItemStack> {
		@Override
		public boolean test(ItemStack itemStack) {
			return true;
		}
	}
}

package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.google.gson.JsonObject;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SmartCardDyeRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	public final Ingredient input;

	public SmartCardDyeRecipe(Ingredient ingredient) {
		this.input = ingredient;
	}

	protected int[] getColor(ItemStack stack) {
		if (!stack.isEmpty()) {
			if (stack.getItem() instanceof IDyeableItem) {
				IDyeableItem targetItem = (IDyeableItem) stack.getItem();

				if (targetItem.hasColor(stack)) {
					int c = targetItem.getColor(stack);
					return new int[]{(c >> 16) & 255, (c >> 8) & 255, c & 255};
				}
			} else {
				Optional<EnumDyeColor> dyeId = DyeUtils.colorFromStack(stack);
				if (dyeId.isPresent()) {
					float[] col = EntitySheep.getDyeRgb(dyeId.get());
					return new int[]{
							(int) (col[0] * 255.0F),
							(int) (col[1] * 255.0F),
							(int) (col[2] * 255.0F)
					};
				}
			}
		}

		return null;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack target = ItemStack.EMPTY;
		List<ItemStack> dyes = new ArrayList<>();

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack source = inv.getStackInSlot(i);
			if (!source.isEmpty()) {
				if (input.apply(source)) {
					if (!target.isEmpty()) {
						return false;
					} else {
						target = source;
					}
				} else if (getColor(source) != null) {
					dyes.add(source);
				} else {
					return false;
				}
			}
		}

		return !target.isEmpty() && !dyes.isEmpty();
	}

	protected Optional<Integer> getMixedColor(InventoryCrafting inv, ItemStack base, Predicate<Integer> slotFilter) {
		int[] color = new int[3];
		int scale = 0;
		int count = 0;

		if (!base.isEmpty()) {
			int[] col = getColor(base);
			if (col != null) {
				scale += Math.max(col[0], Math.max(col[1], col[2]));
				color[0] += col[0];
				color[1] += col[1];
				color[2] += col[2];
				count++;
			}
		}

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (slotFilter == null || slotFilter.test(i)) {
				ItemStack source = inv.getStackInSlot(i);
				if (!source.isEmpty() && source != base) {
					int[] col = getColor(source);
					if (col != null) {
						scale += Math.max(col[0], Math.max(col[1], col[2]));
						color[0] += col[0];
						color[1] += col[1];
						color[2] += col[2];
						count++;
					}
				}
			}
		}

		if (count > 0) {
			int i1 = color[0] / count;
			int j1 = color[1] / count;
			int k1 = color[2] / count;
			float f3 = (float) scale / (float) count;
			float f4 = (float) Math.max(i1, Math.max(j1, k1));
			i1 = (int) (i1 * f3 / f4);
			j1 = (int) (j1 * f3 / f4);
			k1 = (int) (k1 * f3 / f4);
			return Optional.of((i1 << 16) + (j1 << 8) + k1);
		}

		return Optional.empty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack target = ItemStack.EMPTY;
		IDyeableItem targetItem = null;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack source = inv.getStackInSlot(i);
			if (!source.isEmpty()) {
				if (source.getItem() instanceof IDyeableItem) {
					targetItem = (IDyeableItem) source.getItem();
					target = source.copy();
					target.setCount(1);
				}
			}
		}

		if (targetItem != null) {
			Optional<Integer> result = getMixedColor(inv, target, null);
			if (result.isPresent()) {
				targetItem.setColor(target, result.get());
				return target;
			}
		}

		return null;
	}

	@Override
	public boolean canFit(int i, int i1) {
		return i * i1 > 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}

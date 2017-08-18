package com.bymarcin.zettaindustries.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class RecipeUtils {
    private RecipeUtils() {

    }

    public static IRecipe createShapedRecipe(ItemStack output, Object... data) {
        return new ShapedOreRecipe(output.getItem().getRegistryName(), output, data);
    }

    public static IRecipe createShapelessRecipe(ItemStack output, Object... data) {
        return new ShapelessOreRecipe(output.getItem().getRegistryName(), output, data);
    }
}

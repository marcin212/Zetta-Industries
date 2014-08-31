package com.bymarcin.zettaindustries.mods.fframes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.bymarcin.zettaindustries.modmanager.IMod;

import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.recipes.RecipeManagers;

public class Frames implements IMod{
	LarvaeFrame frame;
	@Override
	public void init() {
		frame = new LarvaeFrame();

	}

	@Override
	public void postInit() {
		ItemStack stick = new ItemStack(GameRegistry.findItem("Forestry", "oakStick"),1);
		ItemStack wax = new ItemStack(GameRegistry.findItem("Forestry", "beeswax"),1);
		Fluid honey = FluidRegistry.getFluid("honey");
		
		if(stick.getItem() != null && wax.getItem() != null && honey != null){
			GameRegistry.registerItem(frame, "larvaeFrame");
			RecipeManagers.carpenterManager.addRecipe(30, new FluidStack(honey, 125), null, new ItemStack(frame), 
				"BSB",
				"SCS",
				"BSB",
				'S', stick, 'B', wax, 'C', "beeComb");
		}
	}
	
}

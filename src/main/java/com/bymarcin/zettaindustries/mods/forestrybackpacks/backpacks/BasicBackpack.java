package com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import forestry.api.storage.EnumBackpackType;
import forestry.api.storage.IBackpackDefinition;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

public abstract class BasicBackpack implements IBackpackDefinition{
	private static List<String> names= new ArrayList<String>();
	protected EnumBackpackType type;
	private Predicate<ItemStack> itemStackPredicate = new ItemTest();

	public BasicBackpack(EnumBackpackType type, String... names) {
		this(type);
		Collections.addAll(this.names, names);
	}
	
	public BasicBackpack(EnumBackpackType type){
		this.type = type;
	}
	

	public abstract String getUniqueName();
	
	@Override
	public String getName(ItemStack backpack) {
		return I18n.translateToLocal("item." + getKey() + ".name");
	}

	public String getKey() {
		return getUniqueName() + type.name();
	}

	public static List<String> getNames() {
		return names;
	}

	@Nonnull
	@Override
	public Predicate<ItemStack> getFilter() {
		return itemStackPredicate;
	}


	public static class ItemTest implements Predicate<ItemStack> {
		@Override
	public boolean test(ItemStack itemstack) {
		if(itemstack!=null && itemstack.getItem()!=null){
			ResourceLocation rl = itemstack.getItem().getRegistryName();
			String name = null;
			System.out.println(rl.getResourceDomain());
			System.out.println(rl.getResourcePath());
			if(rl!=null){
				name = rl.getResourcePath();
			}
			if(name==null || name.isEmpty()) return false;
			for(String names : getNames()){
				if(name.startsWith(names)){
					return true;
				}
			}
		}
		return false;
	}
	}
}

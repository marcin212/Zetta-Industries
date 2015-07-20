package com.bymarcin.zettaindustries.mods.forestrybackpacks.backpacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import forestry.api.storage.EnumBackpackType;
import forestry.api.storage.IBackpackDefinition;

public abstract class BasicBackpack implements IBackpackDefinition{
	private List<String> names= new ArrayList<String>();
	protected EnumBackpackType type;
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
		return StatCollector.translateToLocal("item." + getKey() + ".name");
	}

	@Override
	public String getKey() {
		return getUniqueName() + type.name();
	}
	
	@Override
	public void addValidItem(ItemStack validItem) {
		
	}

	@Override
	public void addValidItems(List<ItemStack> validItems) {

	}

	@Override
	public boolean isValidItem(ItemStack itemstack) {
		if(itemstack!=null && itemstack.getItem()!=null){
			String name = Item.itemRegistry.getNameForObject(itemstack.getItem());
			if(name==null || name.isEmpty()) return false;
			for(String names : this.names){
				if(name.startsWith(names)){
					return true;
				}
			}
		}
		return false;
	}

}

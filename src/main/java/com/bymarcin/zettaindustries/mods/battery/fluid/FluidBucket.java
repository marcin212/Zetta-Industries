package com.bymarcin.zettaindustries.mods.battery.fluid;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;


public class FluidBucket extends ItemBucket{
	
	public FluidBucket(Block fluid) {
		super(fluid);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setUnlocalizedName("acidbucket");
	}
	
	@Override
	public void registerIcons(IIconRegister icon) {
		itemIcon = icon.registerIcon(ZettaIndustries.MODID + ":bucket_acid");
		
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
			return itemIcon;
	}

}

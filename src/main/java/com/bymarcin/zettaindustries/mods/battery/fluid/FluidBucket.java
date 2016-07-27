package com.bymarcin.zettaindustries.mods.battery.fluid;

import com.bymarcin.zettaindustries.ZettaIndustries;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;

public class FluidBucket extends ItemBucket {
    public FluidBucket(Block fluid) {
        super(fluid);
        setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
        setUnlocalizedName("acidbucket");
    }

//	@Override
//	public void registerIcons(IIconRegister icon) {
//		itemIcon = icon.registerIcon(ZettaIndustries.MODID + ":bucket_acid");
//	}

}

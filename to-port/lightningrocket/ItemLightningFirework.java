package com.bymarcin.zettaindustries.mods.lightningrocket;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicItem;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLightningFirework extends BasicItem{

	public ItemLightningFirework() {
		super("lightningfirework");
		iconString = ZettaIndustries.MODID + ":lightning_rocket";
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, new LightningFireworkDispenseHandler());
	}

    public boolean onItemUse(ItemStack itemstack, EntityPlayer p_77648_2_, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!world.isRemote)
        {
            EntityFireworkRocket entityfireworkrocket = new EntityLightningFirework(world, (double)((float)p_77648_4_ + p_77648_8_), (double)((float)p_77648_5_ + p_77648_9_), (double)((float)p_77648_6_ + p_77648_10_), itemstack);
            world.spawnEntityInWorld(entityfireworkrocket);

            if (!p_77648_2_.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {

    }
}

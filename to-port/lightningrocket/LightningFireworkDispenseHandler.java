package com.bymarcin.zettaindustries.mods.lightningrocket;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class LightningFireworkDispenseHandler extends BehaviorDefaultDispenseItem{
	
	@Override
	protected ItemStack dispenseStack(IBlockSource dispenser, ItemStack stack) {	
        if (dispenser.getWorld().isRemote) return stack;
        	EnumFacing enumfacing = BlockDispenser.func_149937_b(dispenser.getBlockMetadata());
            EntityFireworkRocket entityfireworkrocket = new EntityLightningFirework(dispenser.getWorld(),
            		enumfacing.getFrontOffsetX()/2f + dispenser.getX(),
            		enumfacing.getFrontOffsetY()/2f + dispenser.getY(),
            		enumfacing.getFrontOffsetZ()/2f + dispenser.getZ(),
            		stack);
            dispenser.getWorld().spawnEntityInWorld(entityfireworkrocket);
            stack.stackSize--;
		return stack;
	}
	
}

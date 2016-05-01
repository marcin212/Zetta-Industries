package com.bymarcin.zettaindustries.basic;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InformationalItemBlock extends ItemBlock {

    public InformationalItemBlock(Block block) {
        super(block);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(stack, player, list, par4);
        if (stack.getItem() instanceof ItemBlock) {
            if (((ItemBlock) stack.getItem()).getBlock() instanceof IBlockInfo) {
                list.addAll(((IBlockInfo) ((ItemBlock)stack.getItem()).getBlock()).getInformation());
            }
        }
    }
}

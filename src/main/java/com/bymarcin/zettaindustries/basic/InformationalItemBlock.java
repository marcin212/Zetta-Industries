package com.bymarcin.zettaindustries.basic;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class InformationalItemBlock extends ItemBlock {

    public InformationalItemBlock(Block block) {
        super(block);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {
        super.addInformation(stack, world, list, flag);
        if (stack.getItem() instanceof ItemBlock) {
            if (((ItemBlock) stack.getItem()).getBlock() instanceof IBlockInfo) {
                list.addAll(((IBlockInfo) ((ItemBlock)stack.getItem()).getBlock()).getInformation());
            }
        }
    }
}

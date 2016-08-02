package com.bymarcin.zettaindustries.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BucketHandler {
    public static BucketHandler INSTANCE = new BucketHandler();
    public Map<Block, Item> buckets = new HashMap<Block, Item>();

    private BucketHandler() {
    }

    
    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
            ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget());

            if (result == null)
                    return;

            event.setResult(Result.ALLOW);
    }

    private ItemStack fillCustomBucket(World world, RayTraceResult pos) {

            IBlockState blockFluidState = world.getBlockState(pos.getBlockPos());
            if(blockFluidState==null) return null;
            Item bucket = buckets.get(blockFluidState.getBlock());
            if (bucket != null && blockFluidState.getBlock().getMetaFromState(blockFluidState)== 0) {
                    world.setBlockToAir(pos.getBlockPos());
                    return new ItemStack(bucket);
            } else
                    return null;

    }
}
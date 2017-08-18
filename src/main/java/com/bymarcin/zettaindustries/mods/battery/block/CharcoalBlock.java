package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.basic.BasicBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CharcoalBlock extends BasicBlock {
    public static class Item extends ItemBlock {
        public Item(Block block) {
            super(block);
        }

        @Override
        public int getItemBurnTime(ItemStack itemStack) {
            return 16000;
        }
    }

    public CharcoalBlock() {
        super(Material.IRON, "charcoalblock");
        setHardness(5.0F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
    }
}

package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.basic.BasicBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class CharcoalBlock extends BasicBlock {

    public CharcoalBlock() {
        super(Material.IRON, "charcoalblock");
        setHardness(5.0F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
    }
}

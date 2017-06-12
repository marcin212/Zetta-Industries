package com.bymarcin.zettaindustries.mods.ocwires.block;


import blusunrize.immersiveengineering.common.blocks.BlockIEBase;

/**
 * Created by Marcin on 16.09.2016.
 */
public enum ConnectorTypes implements BlockIEBase.IBlockEnum {
    TELECOMMUNICATION
    ;
    @Override
    public String getName() {
        return toString().toLowerCase();
    }

    @Override
    public int getMeta() {
        return ordinal();
    }

    @Override
    public boolean listForCreative() {
        return true;
    }
}
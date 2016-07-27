package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityElectrode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBigBatteryElectrode extends BasicBlockMultiblockBase {
    public BlockBigBatteryElectrode() {
        super("batteryelectrode");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.inside"));
        info.add(localize("tooltip.electrode1"));
        info.add(localize("tooltip.electrode2"));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityElectrode();
    }

}

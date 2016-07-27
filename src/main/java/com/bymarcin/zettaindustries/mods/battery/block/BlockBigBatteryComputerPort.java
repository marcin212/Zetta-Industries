package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityComputerPort;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockBigBatteryComputerPort extends BasicBlockMultiblockBase {

    public BlockBigBatteryComputerPort() {
        super("batterycomputerport");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.sides"));
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityComputerPort();
    }


}

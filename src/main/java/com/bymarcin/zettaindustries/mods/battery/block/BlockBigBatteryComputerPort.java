package com.bymarcin.zettaindustries.mods.battery.block;

import javax.annotation.Nullable;

import com.bymarcin.zettaindustries.mods.battery.AdvancedStorage;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.tileentity.BatteryController;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityComputerPort;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityControler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
    
    
    
    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        TileEntity te = blockAccess.getTileEntity(pos);
        if(te instanceof TileEntityComputerPort){
            MultiblockControllerBase controller = ((TileEntityComputerPort) te).getMultiblockController();
            if(controller instanceof BatteryController){
                return ((BatteryController) controller).getRedstoneSignal();
            }
        }
        return getWeakPower(blockState, blockAccess, pos, side);
    }
    
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }
    
    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return side != EnumFacing.UP && side!= EnumFacing.DOWN;
    }
}

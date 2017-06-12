package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityControler;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBigBatteryController extends BasicBlockMultiblockBase {
    public static final PropertyInteger OnOff = PropertyInteger.create("on_off", 0,1);
    public static final int ON = 1;
    public static final int OFF = 0;

    public BlockBigBatteryController() {
        super("batterycontroller");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.sides"));
        info.add(localize("tooltip.controller1"));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityControler();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return false;
        }

        if (world.getTileEntity(pos) != null &&
                ((TileEntityControler) world.getTileEntity(pos)).getMultiblockController() != null &&
                ((TileEntityControler) world.getTileEntity(pos)).getMultiblockController().isAssembled()) {
            player.openGui(ZettaIndustries.instance, 3, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(OnOff, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(OnOff);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, OnOff);
    }
}

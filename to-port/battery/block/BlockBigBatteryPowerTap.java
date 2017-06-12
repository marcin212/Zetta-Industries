package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityPowerTap;
import net.minecraft.block.properties.PropertyBool;
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


public class BlockBigBatteryPowerTap extends BasicBlockMultiblockBase {
    public static final PropertyInteger IO = PropertyInteger.create("io",0,1);
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    public BlockBigBatteryPowerTap() {
        super("batterypowertap");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.top"));
        info.add(localize("tooltip.powertap1"));
        info.add(localize("tooltip.powertap2"));
        info.add(localize("tooltip.powertap3"));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            if (!world.isRemote) {
                player.openGui(ZettaIndustries.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }

        if (heldItem == null && player.isSneaking()) {
			if (state.getValue(IO) == INPUT) {
				world.setBlockState(pos, state.withProperty(IO, OUTPUT), 2);
			} else {
                world.setBlockState(pos, state.withProperty(IO, INPUT), 2);
			}
            return true;
        }
        return false;
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IO);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IO);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return meta == OUTPUT ? getDefaultState().withProperty(IO, OUTPUT) : getDefaultState().withProperty(IO, INPUT);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityPowerTap();
    }

}

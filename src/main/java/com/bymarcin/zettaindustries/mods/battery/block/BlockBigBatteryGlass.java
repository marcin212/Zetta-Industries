package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityGlass;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBigBatteryGlass extends BasicBlockMultiblockBase {

    public BlockBigBatteryGlass() {
        super("batteryglass", Material.GLASS);
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.sides"));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityGlass();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }
}

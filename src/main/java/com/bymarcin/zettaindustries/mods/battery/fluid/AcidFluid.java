package com.bymarcin.zettaindustries.mods.battery.fluid;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;


public class AcidFluid extends BlockFluidClassic  {
    public static final ResourceLocation stillIcon = new ResourceLocation(ZettaIndustries.MODID,"blocks/battery/fluidAcidStill");
    public static final ResourceLocation flowingIcon = new ResourceLocation(ZettaIndustries.MODID,"blocks/battery/fluidAcidFlowing");

    public AcidFluid(Fluid fluid) {
        super(fluid, new MaterialLiquid(MapColor.YELLOW));
        this.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
        this.setRegistryName("sulfurousacid");
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().isLiquid() && super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().isLiquid() && super.displaceIfPossible(world, pos);
    }


}

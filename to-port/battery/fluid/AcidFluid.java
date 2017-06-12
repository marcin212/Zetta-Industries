package com.bymarcin.zettaindustries.mods.battery.fluid;

import com.bymarcin.zettaindustries.ZettaIndustries;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;


public class AcidFluid extends BlockFluidClassic {
    public static final ResourceLocation stillIcon = new ResourceLocation(ZettaIndustries.MODID, "blocks/battery/fluidAcidStill");
    public static final ResourceLocation flowingIcon = new ResourceLocation(ZettaIndustries.MODID, "blocks/battery/fluidAcidFlowing");

    public AcidFluid(Fluid fluid) {
        super(fluid, Material.WATER);
        this.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
        this.setRegistryName("sulfurousacid");
        this.setUnlocalizedName("sulfurousacid");
        setHardness(101f);
        setLightOpacity(3);
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock().getMaterial(blockState).isLiquid()) {
            return false;
        }
        return super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock().getMaterial(blockState).isLiquid()) {
            return false;
        }
        return super.displaceIfPossible(world, pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.drown, 1);
    }
}

package com.bymarcin.zettaindustries.mods.battery.fluid;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class AcidFluid extends BlockFluidClassic{
    @SideOnly(Side.CLIENT)
    private IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    private IIcon flowingIcon;
    
	public AcidFluid(Fluid fluid) {
		super(fluid, MaterialLiquid.water);
		  this.setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		  this.setBlockName("sulfurousacid");
	}

    @Override
    public IIcon getIcon(int side, int meta) {
            return (side == 0 || side == 1)? stillIcon : flowingIcon;
    }
    
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister register) {
        stillIcon = register.registerIcon(ZettaIndustries.MODID+":battery/fluidAcidStill");
        flowingIcon = register.registerIcon(ZettaIndustries.MODID+":battery/fluidAcidFlowing");
	
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
    }
   
    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
    }
}

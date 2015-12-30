package com.bymarcin.zettaindustries.mods.bundledviewer;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.utils.render.RenderUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class BundledViewerBlock extends BlockContainer{

	public BundledViewerBlock() {
		super(Material.iron);
		setBlockName("BundledViewer");
		textureName = ZettaIndustries.MODID+":bundledviewer";
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
	}

	@Override
	public TileEntity createNewTileEntity(World w, int metadata) {
		return new BundledViewerTileEntity();
	}
	
	@Override
	public int getRenderType() {
		return super.getRenderType();
	}
	
    @Override
    public boolean isOpaqueCube(){
        return false;
    }
    
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
	public void onNeighborBlockChange(World w, int x, int y, int z, Block b) {
		super.onNeighborBlockChange(w, x, y, z, b);
		TileEntity te = w.getTileEntity(x, y, z);
		if(te instanceof BundledViewerTileEntity){
			((BundledViewerTileEntity)te).onBundledInputChanged();
		}
	}
	
	///
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return RenderUtils.FORGE_DIRECTIONS[world.getBlockMetadata(x, y, z)] == side;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return true;
	}
}

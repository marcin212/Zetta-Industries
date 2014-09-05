package com.bymarcin.zettaindustries.mods.rfpowermeter;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.basic.BasicBlockContainer;

public class RFMeterBlock extends BasicBlockContainer{
	public RFMeterBlock() {
		super(Material.iron, "rfmeterblock");
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        
        if (meta == 0)
        {
        	this.setBlockBounds(2/16F-0.001F, 0/16F-0.001F, 0/16F-0.001F, 14/16F+0.001F, 16/16F+0.001F, 14/16F+0.001F);
        }

        if (meta == 3)
        {
        	this.setBlockBounds(0/16F, 0/16F, 2/16F, 14/16F, 16/16F, 14/16F);
        }
        
        if (meta == 2)
        {
            this.setBlockBounds(2/16F, 0/16F, 2/16F, 14/16F, 16/16F, 16/16F);
        }
        
        if (meta == 1)
        {
        	this.setBlockBounds(2/16F, 0/16F, 2/16F, 16/16F, 16/16F, 14/16F);
        }
    }
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new RFMeterTileEntity();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
		int whichDirectionFacing = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		
		if(player.getHeldItem()!=null && player.getHeldItem().getItem() instanceof ItemDye){
			RFMeterTileEntity te = (RFMeterTileEntity) world.getTileEntity(x, y, z);
			if(te!=null){
				int color = ItemDye.field_150922_c[player.getHeldItem().getItemDamage()];
				te.r = (float)(color >> 16 & 255) / 255.0F;
				te.g = (float)(color >> 8 & 255) / 255.0F;
				te.b = (float)(color & 255) / 255.0F;
				return true;
			}
	   	}
		return false;
	}
}

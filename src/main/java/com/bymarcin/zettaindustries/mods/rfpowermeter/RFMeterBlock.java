package com.bymarcin.zettaindustries.mods.rfpowermeter;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.basic.BasicBlockContainer;

public class RFMeterBlock extends BasicBlockContainer{
	public RFMeterBlock() {
		super(Material.iron, "rfmeterblock");
		
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return Blocks.iron_block.getIcon(side, meta);
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		// We don't want it to register here.
	}

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        
        if (meta == 0)
        {
        	this.setBlockBounds(2/16F-0.001F, 0, 0, 14/16F+0.001F, 1, 14/16F+0.001F);
        	return;
        }

        if (meta == 3)
        {
        	this.setBlockBounds(0, 0, 2/16F-0.001F, 14/16F+0.001F, 1, 14/16F+0.001F);
        	return;
        }
        
        if (meta == 2)
        {
            this.setBlockBounds(2/16F-0.001F, 0, 2/16F-0.001F, 14/16F+0.001F, 1, 1);
            return;
        }
        
        if (meta == 1)
        {
        	this.setBlockBounds(2/16F-0.001F, 0, 2/16F-0.001F, 1, 1, 14/16F+0.001F);
        	return;
        }
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return RFMeter.isOCAllowed ? new RFMeterTileEntityOC() : new RFMeterTileEntity();
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
				world.markBlockForUpdate(x, y, z);
				return true;
			}
	   	}
		
		if(player.getHeldItem()==null && player.isSneaking()){
			RFMeterTileEntity te = (RFMeterTileEntity) world.getTileEntity(x, y, z);
			if(te!=null){
				te.invert();
				world.markBlockForUpdate(x, y, z);
			}
			return true;
		}
		return false;
	}

	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
	    ArrayList<ItemStack> items = getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	    if (world.setBlockToAir(x, y, z))
	    {
	      if (!world.isRemote) {
	        for (ItemStack item : items) {
	          if ((player == null) || (!player.capabilities.isCreativeMode) || (item.hasTagCompound())) {
	        	  dropBlockAsItem(world, x, y, z, item);
	          }
	        }
	      }
	      return true;
	    }
	    return false;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ItemStack stack = new ItemStack(this);
		 RFMeterTileEntity tile = (RFMeterTileEntity) world.getTileEntity(x, y, z);
	        if (tile != null)
	        {
	           stack.stackTagCompound = new NBTTagCompound();
		       tile.getTag(stack.stackTagCompound);
	        }
	     ret.add(stack);
		return ret;
	}
	
	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int p_149690_5_, float p_149690_6_, int p_149690_7_) {

	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
}


package com.bymarcin.zettaindustries.mods.mgc.item;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.block.ElectricalConnectorBlock;
import com.bymarcin.zettaindustries.mods.mgc.block.LampSocketBlock;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalConnectorTileEntity;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class LampSocketItem extends ItemBlock {
	
	public LampSocketItem(Block b) {
		super(b);
	}
	
	@Override
	public int getMetadata (int damageValue)
	{
		return damageValue;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		this.field_150939_a.getSubBlocks(item, tab, itemList);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName()+"."+itemstack.getItemDamage();
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, meta);
		
		if(!ret)
			return ret;
		if(world.getTileEntity(x, y, z) instanceof LampSocketTileEntity)
			if(meta==LampSocketBlock.chandelier){
				((LampSocketTileEntity)world.getTileEntity(x, y, z)).setFacing(ForgeDirection.UP.ordinal());
			}else{
				((LampSocketTileEntity)world.getTileEntity(x, y, z)).setFacing(ForgeDirection.getOrientation(side).ordinal());
			}
		return ret;
	}
	
}

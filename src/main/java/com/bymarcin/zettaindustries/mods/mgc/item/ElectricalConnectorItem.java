package com.bymarcin.zettaindustries.mods.mgc.item;

import java.util.List;

import com.bymarcin.zettaindustries.mods.mgc.block.ElectricalConnectorBlock;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalConnectorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class ElectricalConnectorItem extends ItemBlock{
	
	public ElectricalConnectorItem(Block block) {
		super(block);
		setHasSubtypes(true);
		
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

		if(meta==ElectricalConnectorBlock.relayHV && world.isAirBlock(x,y+1,z))
			return false;
		if(meta==ElectricalConnectorBlock.connectorLV||meta==ElectricalConnectorBlock.connectorMV||meta==ElectricalConnectorBlock.connectorHV)
		{
			ForgeDirection fd = ForgeDirection.getOrientation(side).getOpposite();
			if(world.isAirBlock(x+fd.offsetX, y+fd.offsetY, z+fd.offsetZ))
				return false;
		}
		
		boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, meta);
		
		if(!ret)
			return ret;
		if(world.getTileEntity(x, y, z) instanceof ElectricalConnectorTileEntity)
			if(meta==ElectricalConnectorBlock.relayHV){
				((ElectricalConnectorTileEntity)world.getTileEntity(x, y, z)).setFacing(ForgeDirection.UP.ordinal());
			}else{
				((ElectricalConnectorTileEntity)world.getTileEntity(x, y, z)).setFacing(ForgeDirection.getOrientation(side).getOpposite().ordinal());
			}
		return ret;
	}
}

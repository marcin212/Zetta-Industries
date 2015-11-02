package com.bymarcin.zettaindustries.mods.eawiring.connectors.items;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.block.EAConnector;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityConnectorBase;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import mods.eln.misc.Coordonate;
import mods.eln.node.simple.SimpleNode;
import mods.eln.node.simple.SimpleNodeBlock;
import mods.eln.node.simple.SimpleNodeItem;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class ItemEAConnector extends SimpleNodeItem{
	
	public ItemEAConnector(Block block) {
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

		if(meta==EAConnector.relayHV && world.isAirBlock(x,y+1,z))
			return false;
		if(meta==EAConnector.connectorLV||meta==EAConnector.connectorMV||meta==EAConnector.connectorHV)
		{
			ForgeDirection fd = ForgeDirection.getOrientation(side).getOpposite();
			if(world.isAirBlock(x+fd.offsetX, y+fd.offsetY, z+fd.offsetZ))
				return false;
		}
		
		boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, meta);
		
		if(!ret)
			return ret;
		if(world.getTileEntity(x, y, z) instanceof TileEntityConnectorBase)
			((TileEntityConnectorBase)world.getTileEntity(x, y, z)).setQFacing(MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3);
			((TileEntityConnectorBase)world.getTileEntity(x, y, z)).setFacing(ForgeDirection.getOrientation(side).getOpposite().ordinal());
		return ret;
	}
	
}

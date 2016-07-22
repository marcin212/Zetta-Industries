package com.bymarcin.zettaindustries.mods.ocwires.item;

import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class ItemBlockTelecommunicationConnector extends ItemBlock{

	public ItemBlockTelecommunicationConnector(Block block) {
		super(block);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, meta);
		if(!ret)
			return ret;
		if(world.getTileEntity(x, y, z) instanceof TileEntityTelecomunicationConnector)
			((TileEntityTelecomunicationConnector)world.getTileEntity(x, y, z)).setFacing(ForgeDirection.getOrientation(side).getOpposite().ordinal());
		return ret;
	}
}

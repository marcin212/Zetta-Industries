package com.bymarcin.zettaindustries.mods.ocwires.item;

import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockTelecommunicationConnector extends ItemBlock{

	public ItemBlockTelecommunicationConnector(Block block) {
		super(block);
	}

//	@Override
//	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
////		boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
////		if(!ret)
////			return ret;
////		if(world.getTileEntity(pos) instanceof TileEntityTelecomunicationConnector)
////			((TileEntityTelecomunicationConnector)world.getTileEntity(pos)).setFacing(side.getOpposite().ordinal());
////		return ret;
//	}

}

package com.bymarcin.zettaindustries.registry.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IGUI {
	
	public Object getServerGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z);

	public Object getClientGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z);
}

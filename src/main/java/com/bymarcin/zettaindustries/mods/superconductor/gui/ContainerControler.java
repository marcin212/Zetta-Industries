package com.bymarcin.zettaindustries.mods.superconductor.gui;

import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityControler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;

public class ContainerControler extends Container{
	TileEntityControler tile;
	
	public ContainerControler(EntityPlayer entityplayer,TileEntityControler controler) {
		tile = controler;
		if(entityplayer instanceof EntityPlayerMP)
			tile.addPlayerToUpdate((EntityPlayerMP)entityplayer);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		if(tile != null){
			tile.removePlayerFromUpdate(par1EntityPlayer);
		}
	}
}

package com.bymarcin.zettaindustries.mods.battery.gui;

import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityPowerTap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class PowerTapContener extends Container{
	TileEntityPowerTap part;
	
	public PowerTapContener(TileEntityPowerTap powerTap, EntityPlayer player) {
			part = powerTap;
			if(powerTap!=null){
				powerTap.beginUpdatingPlayer(player);
			}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void putStackInSlot(int slot, ItemStack stack) {
    }

	@Override
	    public void onContainerClosed(EntityPlayer player) {
			super.onContainerClosed(player);
			if(part != null)
				part.stopUpdatingPlayer(player);
	}

}

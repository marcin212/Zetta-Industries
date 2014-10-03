package com.bymarcin.zettaindustries.mods.battery.gui;

import com.bymarcin.zettaindustries.mods.battery.tileentity.BatteryController;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityControler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class BigBatteryContainer extends Container{
		TileEntityControler part;

		public BigBatteryContainer(TileEntityControler batteryControler, EntityPlayer player) {
			part = batteryControler;
			if(part.getMultiblockController() !=null){
				((BatteryController)part.getMultiblockController()).beginUpdatingPlayer(player);
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
				if(part != null && part.getMultiblockController() != null)
					((BatteryController)part.getMultiblockController()).stopUpdatingPlayer(player);
		}
}


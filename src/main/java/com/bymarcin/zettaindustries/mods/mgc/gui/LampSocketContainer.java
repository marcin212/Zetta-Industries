package com.bymarcin.zettaindustries.mods.mgc.gui;

import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class LampSocketContainer extends Container {
	protected LampSocketTileEntity tileEntity;

	public LampSocketContainer(InventoryPlayer inventoryPlayer, LampSocketTileEntity te) {
		tileEntity = te;
		addSlotToContainer(new SlotAcceptValid(tileEntity, 0, 80, 20));
		// commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public void onContainerClosed(EntityPlayer p) {
		super.onContainerClosed(p);
		if(tileEntity!=null){
			tileEntity.closeInventory();
		}
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 51 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 109));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return null;
	}
}

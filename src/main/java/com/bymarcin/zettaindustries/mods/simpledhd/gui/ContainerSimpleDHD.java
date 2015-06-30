package com.bymarcin.zettaindustries.mods.simpledhd.gui;

import com.bymarcin.zettaindustries.mods.simpledhd.tileentity.TileEntitySimpleDHD;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerSimpleDHD extends Container{
	TileEntitySimpleDHD entity;
	
	public ContainerSimpleDHD(TileEntitySimpleDHD entity){
		this.entity = entity;
	}
	
	public void setAddress(String text){
		if(entity!=null){
			entity.setAddress(text);
		}
	}
	
	public String getAddress(){
		return entity!=null?entity.getAddress():"";
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	public TileEntitySimpleDHD getTileEntity() {
		return entity;
	}

}

package com.bymarcin.zettaindustries.registry.gui;

import com.bymarcin.zettaindustries.registry.ZIRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler extends ZIRegistry implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
   	 TileEntity tileEntity = world.getTileEntity(x, y, z);
   	 Object temp = null;
   	 for(IGUI g : gui){
   		temp = g.getServerGuiElement(ID, tileEntity, player, world, x, y, z);
   		if(temp != null) return temp;
   	 }	
   	return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        Object temp = null;
       	for(IGUI g :  gui){
    		 temp = g.getClientGuiElement(ID, tileEntity, player, world, x, y, z);
    		 if(temp != null) return temp;
       	}
        return null;
	}

}

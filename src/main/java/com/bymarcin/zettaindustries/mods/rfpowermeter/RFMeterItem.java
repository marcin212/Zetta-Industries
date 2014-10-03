package com.bymarcin.zettaindustries.mods.rfpowermeter;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RFMeterItem extends ItemBlock{

	public RFMeterItem(Block block) {
		super(block);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		if(super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)){
            RFMeterTileEntity tile = (RFMeterTileEntity) world.getTileEntity(x, y, z);
            if (tile != null && stack.stackTagCompound != null)
            {
                tile.setTag(stack.stackTagCompound);
            }
            return true;
		}
        return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List info, boolean p_77624_4_) {
		super.addInformation(stack, p_77624_2_, info, p_77624_4_);
		if(stack.stackTagCompound !=null){
			
				if(stack.stackTagCompound.hasKey("name") && !stack.stackTagCompound.getString("name").isEmpty())
					info.add("Name: " + stack.stackTagCompound.getString("name"));
			
				if(stack.stackTagCompound.hasKey("value"))
					info.add("RF: "  + stack.stackTagCompound.getLong("value"));

				if(stack.stackTagCompound.hasKey("isProtected"))
					info.add("isProtected: " + stack.stackTagCompound.getBoolean("isProtected"));
		}
	}
}

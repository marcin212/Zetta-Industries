package com.bymarcin.zettaindustries.mods.rfpowermeter;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RFMeterItem extends ItemBlock{

	public RFMeterItem(Block block) {
		super(block);
	}

    @Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)){
            RFMeterTileEntity tile = (RFMeterTileEntity) world.getTileEntity(pos);
            if (tile != null && stack.getTagCompound() != null)
            {
                tile.setTag(stack.getTagCompound());
            }
            return true;
		}
        return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World p_77624_2_, List info, ITooltipFlag p_77624_4_) {
		super.addInformation(stack, p_77624_2_, info, p_77624_4_);
		if(stack.hasTagCompound()){
			
				if(RFMeter.isOCAllowed && stack.getTagCompound().hasKey("name") && !stack.getTagCompound().getString("name").isEmpty())
					info.add("Name: " + stack.getTagCompound().getString("name"));
			
				if(stack.getTagCompound().hasKey("value"))
					info.add("RF: "  + stack.getTagCompound().getLong("value"));

				if(RFMeter.isOCAllowed && stack.getTagCompound().hasKey("isProtected"))
					info.add("isProtected: " + stack.getTagCompound().getBoolean("isProtected"));
		}
	}
}

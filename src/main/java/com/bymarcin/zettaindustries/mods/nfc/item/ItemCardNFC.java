package com.bymarcin.zettaindustries.mods.nfc.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCardNFC extends BasicItem{
	public ItemCardNFC() {
		super("itemcardnfc");
		setMaxStackSize(1);
	}
	
	public static void setNFCData(String NFCData, ItemStack stack) {
		if(stack.stackTagCompound==null)
			stack.setTagCompound( new NBTTagCompound( ) );
		stack.stackTagCompound.setString("NFCData", NFCData);
	}
	
	public static String getNFCData(ItemStack stack) {
		if(stack.stackTagCompound!=null){
			String data = stack.stackTagCompound.getString("NFCData");
			return data!=null?data:"";
		}else{
			return "";
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon) {
		itemIcon = icon.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_item");
	}
	
}

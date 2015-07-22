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
	
	public static void setNFCData(byte[] NFCData, ItemStack stack) {
		if(stack.stackTagCompound==null)
			stack.setTagCompound( new NBTTagCompound( ) );
		stack.stackTagCompound.setByteArray("NFCDataByte", NFCData);
	}
	
	public static byte[] getNFCData(ItemStack stack) {
		if(stack.stackTagCompound!=null){
			if(stack.stackTagCompound.hasKey("NFCData")){
				stack.stackTagCompound.setByteArray("NFCDataByte", stack.stackTagCompound.getString("NFCData").getBytes());
				stack.stackTagCompound.removeTag("NFCData");
			}
			
			byte[] data = stack.stackTagCompound.getByteArray("NFCDataByte");
			return data!=null?data:new byte[]{};
		}else{
			return new byte[]{};
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon) {
		itemIcon = icon.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_item");
	}
	
}

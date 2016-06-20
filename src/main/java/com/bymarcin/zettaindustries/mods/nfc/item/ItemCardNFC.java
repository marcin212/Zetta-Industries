package com.bymarcin.zettaindustries.mods.nfc.item;

import com.bymarcin.zettaindustries.basic.BasicItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class ItemCardNFC extends BasicItem {
    public ItemCardNFC() {
        this("itemcardnfc");
    }

    public ItemCardNFC(String registryName) {
        super(registryName);
        setMaxStackSize(1);
    }

    public static void setNFCData(byte[] NFCData, ItemStack stack) {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setByteArray("NFCDataByte", NFCData);
    }

    public static byte[] getNFCData(ItemStack stack) {
        if (stack.getTagCompound() != null) {
            if (stack.getTagCompound().hasKey("NFCData")) {
                stack.getTagCompound().setByteArray("NFCDataByte", stack.getTagCompound().getString("NFCData").getBytes());
                stack.getTagCompound().removeTag("NFCData");
            }

            byte[] data = stack.getTagCompound().getByteArray("NFCDataByte");
            return data != null ? data : new byte[]{};
        } else {
            return new byte[]{};
        }
    }
}

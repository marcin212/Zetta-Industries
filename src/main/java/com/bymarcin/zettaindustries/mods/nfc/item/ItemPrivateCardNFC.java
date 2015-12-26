package com.bymarcin.zettaindustries.mods.nfc.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.ZettaIndustries;

public class ItemPrivateCardNFC extends ItemCardNFC{
	IIcon ItemIconOwner;
	IIcon ItemIconNotOwner;
	
	public ItemPrivateCardNFC() {
		setUnlocalizedName("itemprivatecardnfc");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemStack,EntityPlayer player,List par3List, boolean par4) {
		if(itemStack.getItem()instanceof ItemPrivateCardNFC && getOwner(itemStack)!=null)
			par3List.add(getOwner(itemStack));
	}

	private static void setOwner(String name, ItemStack item){
		if(item.stackTagCompound==null)
			item.setTagCompound(new NBTTagCompound());
		if(name!=null)
			item.stackTagCompound.setString("ownerNFC", name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World par2World,
			EntityPlayer player) {
		if(itemStack.getItem() instanceof ItemPrivateCardNFC && player.isSneaking() && getOwner(itemStack)==null){
			setOwner(player.getGameProfile().getName(), itemStack);
		}
		return itemStack;
	}
	
	public static String getOwner(ItemStack item) {
		if(item.stackTagCompound!=null)
			return item.stackTagCompound.getString("ownerNFC");
		else
			return null;
	}
	
	@Override
	public void registerIcons(IIconRegister icon) {
		ItemIconNotOwner = icon.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_item_not_owner");
		ItemIconOwner = icon.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_item_owner");
		super.registerIcons(icon);
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if(getOwner(stack)==null)
			return ItemIconNotOwner;
		else
			return ItemIconOwner;
	}
	
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		return getIcon(stack,0);
	}
}

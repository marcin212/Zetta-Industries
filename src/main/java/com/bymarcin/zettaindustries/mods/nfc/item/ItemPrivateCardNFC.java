package com.bymarcin.zettaindustries.mods.nfc.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.ZettaIndustries;

public class ItemPrivateCardNFC extends ItemCardNFC{

	public ItemPrivateCardNFC() {
		super("itemprivatecardnfc");
		setUnlocalizedName("itemprivatecardnfc");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemStack,EntityPlayer player,List par3List, boolean par4) {
		if(itemStack.getItem()instanceof ItemPrivateCardNFC && getOwner(itemStack)!=null)
			par3List.add(getOwner(itemStack));
	}

	private static void setOwner(String name, ItemStack item){
		if(item.getTagCompound()==null)
			item.setTagCompound(new NBTTagCompound());
		if(name!=null)
			item.getTagCompound().setString("ownerNFC", name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		if(itemStack.getItem() instanceof ItemPrivateCardNFC && player.isSneaking() && getOwner(itemStack)==null){
			setOwner(player.getName(), itemStack);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS,itemStack);
	}
	
	public static String getOwner(ItemStack item) {
		if(item.getTagCompound()!=null)
			return item.getTagCompound().getString("ownerNFC");
		else
			return null;
	}



//	@Override
//	public void registerIcons(IIconRegister icon) {
//		ItemIconNotOwner = icon.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_item_not_owner");
//		ItemIconOwner = icon.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_item_owner");
//		super.registerIcons(icon);
//	}
//
//	@Override
//	public IIcon getIcon(ItemStack stack, int pass) {
//		if(getOwner(stack)==null)
//			return ItemIconNotOwner;
//		else
//			return ItemIconOwner;
//	}
//
//	@Override
//	public IIcon getIconIndex(ItemStack stack) {
//		return getIcon(stack,0);
//	}
}

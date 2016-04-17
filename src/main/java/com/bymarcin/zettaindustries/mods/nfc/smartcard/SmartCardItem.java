package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;

import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.registry.GameRegistry;

public class SmartCardItem extends Item implements li.cil.oc.api.driver.Item{
	public static final String PRIVATE_KEY = "sc:private_key";
	public static final String PUBLIC_KEY = "sc:public_key";
	public static final String OWNER = "sc:owner";
	public static SecureRandom srand;

	public SmartCardItem() {
		try {
			srand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setUnlocalizedName("smartcard");
		setMaxStackSize(1);
		setNoRepair();
		setTextureName(ZettaIndustries.MODID + ":nfc/smart_card");
		GameRegistry.registerItem(this, "SmartCard");
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer p, List list, boolean q) {
		super.addInformation(stack, p, list, q);
		
		String info = getOwner(stack);
		if(info != null && !info.isEmpty())
			list.add("Protected");
	}
	
	public static void generateKeyPair(NBTTagCompound tag){
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
			kpg.initialize(384, srand);
			KeyPair kp = kpg.generateKeyPair();
			tag.setByteArray(PRIVATE_KEY, kp.getPrivate().getEncoded());
			tag.setByteArray(PUBLIC_KEY, kp.getPublic().getEncoded());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static NBTTagCompound getNBT(ItemStack stack) {
		if (stack.hasTagCompound()) {
			if(!stack.getTagCompound().hasKey(PRIVATE_KEY) || !stack.getTagCompound().hasKey(PUBLIC_KEY)){
				generateKeyPair(stack.getTagCompound());
			}
			return stack.getTagCompound();
		} else {
			NBTTagCompound tag = new NBTTagCompound();
			generateKeyPair(tag);
			stack.setTagCompound(tag);
			return tag;
		}
	}
	
	public static byte[] getPrivateKey(ItemStack stack) {
			return getNBT(stack).getByteArray(PRIVATE_KEY);
	}

	public static byte[] getPublicKey(ItemStack stack) {
		return getNBT(stack).getByteArray(PUBLIC_KEY);
	}

	public static String getOwner(ItemStack stack) {
		return getNBT(stack).getString(OWNER);
	}

	@Override
	public boolean worksWith(ItemStack stack) {
		return stack.getItem().equals(this);
	}

	@Override
	public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
		return null;
	}

	@Override
	public String slot(ItemStack stack) {
		return Slot.Any;
	}

	@Override
	public int tier(ItemStack stack) {
		return 0;
	}

	@Override
	public NBTTagCompound dataTag(ItemStack stack) {
		return new NBTTagCompound();
	}
}

package com.bymarcin.zettaindustries.mods.mgc.item;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

public class LightBulbItem extends Item {
	public static final int START_INCANDESCENT = 0;
	public static final int START_ECO = 1;
	IIcon[] icons;
	String[] iconsName = new String[] { ":mgc/50WLightBulb", ":mgc/100WLightBulb", ":mgc/8WEcoLightBulb", ":mgc/15WEcoLightBulb" };
	static NBTTagCompound[] lightBulbTags;

	public LightBulbItem() {
		setHasSubtypes(true);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setMaxStackSize(1);
		setUnlocalizedName("LightBulb");

		lightBulbTags = new NBTTagCompound[] {
				createLightBulb(50, 200, 60 * 60 * 12, 8, LightBulbItem.START_INCANDESCENT),
				createLightBulb(100, 200, 60 * 60 * 12, 15, LightBulbItem.START_INCANDESCENT),
				createLightBulb(8, 200, 60 * 60 * 48, 8, LightBulbItem.START_ECO),
				createLightBulb(15, 200, 60 * 60 * 48, 15, LightBulbItem.START_ECO)
		};

		GameRegistry.registerItem(this, "LightBulb");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < iconsName.length; i++) {
			ItemStack stack = new ItemStack(item, 1, i);
			stack.setTagCompound((NBTTagCompound) lightBulbTags[i].copy());
			list.add(stack);
		}
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		super.addInformation(itemStack, entityPlayer, list, par4);
		list.add("U: " + LightBulbItem.getVoltage(itemStack) + "V");
		list.add("P: " + LightBulbItem.getPower(itemStack) + "W");
		list.add("L: " + LightBulbItem.getLightValue(itemStack) + "Blocks");
	}

	@Override
	public void registerIcons(IIconRegister iconRegistry) {
		icons = new IIcon[iconsName.length];
		for (int i = 0; i < iconsName.length; i++) {
			icons[i] = iconRegistry.registerIcon(ZettaIndustries.MODID + iconsName[i]);
		}
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return icons[metadata];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName() + "." + itemstack.getItemDamage();
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}

	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		getTagCompound(itemStack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getLightBulbDamage(stack) != getNomialLife(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - (getLightBulbDamage(stack) / (double) getNomialLife(stack));
	}

	public static NBTTagCompound getTagCompound(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound((NBTTagCompound) lightBulbTags[stack.getItemDamage()].copy());
		}
		return stack.stackTagCompound;
	}

	public static int applyLightBulbDamage(ItemStack stack) {
		int newValue = getTagCompound(stack).getInteger("LightBulbDamage") - 1;
		getTagCompound(stack).setInteger("LightBulbDamage", newValue);
		return newValue;
	}

	public static int getStartType(ItemStack stack) {
		return getTagCompound(stack).getInteger("StartType");
	}

	public static int getLightBulbDamage(ItemStack stack) {
		return getTagCompound(stack).getInteger("LightBulbDamage");
	}

	public static int getNomialLife(ItemStack stack) {
		return getTagCompound(stack).getInteger("NomialLife");
	}

	public static int getLightValue(ItemStack stack) {
		return getTagCompound(stack).getInteger("LightValue");
	}

	public static double getPower(ItemStack stack) {
		return getTagCompound(stack).getDouble("Power");
	}

	public static double getVoltage(ItemStack stack) {
		return getTagCompound(stack).getDouble("Voltage");
	}

	public static NBTTagCompound createLightBulb(double P, double U, int nomialLife, int lightLevel, int startType) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("LightBulbDamage", nomialLife);
		tag.setInteger("LightValue", lightLevel);
		tag.setInteger("StartType", startType);
		tag.setDouble("Voltage", U);
		tag.setDouble("Power", P);
		tag.setInteger("NomialLife", nomialLife);
		return tag;
	}
}

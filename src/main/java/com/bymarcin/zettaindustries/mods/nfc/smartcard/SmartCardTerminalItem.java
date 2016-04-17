package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import java.util.Set;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.nfc.NFC;

import org.lwjgl.opengl.GL11;

import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.item.HostAware;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.driver.item.UpgradeRenderer;
import li.cil.oc.api.event.RackMountableRenderEvent;
import li.cil.oc.api.event.RobotRenderEvent.MountPoint;
import li.cil.oc.api.internal.Rack;
import li.cil.oc.api.internal.Robot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import dan200.computercraft.ComputerCraft.Blocks;

public class SmartCardTerminalItem extends Item implements EnvironmentProvider, HostAware, li.cil.oc.api.driver.Item {

	public SmartCardTerminalItem() {
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		GameRegistry.registerItem(this, "SmartCardTerminal");
		setUnlocalizedName("smartcardterminal");
		setMaxStackSize(1);
		setTextureName(ZettaIndustries.MODID + ":nfc/smart_card_terminal_item");
	}

	@Override
	public Class<?> getEnvironment(ItemStack stack) {
		return SmartCardTerminal.class;
	}

	@Override
	public boolean worksWith(ItemStack stack) {
		return stack.getItem().equals(this);
	}

	@Override
	public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
		return new SmartCardTerminal((Rack) host);
	}

	@Override
	public String slot(ItemStack stack) {
		return Slot.RackMountable;
	}

	@Override
	public int tier(ItemStack stack) {
		return 1;
	}

	@Override
	public NBTTagCompound dataTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		final NBTTagCompound nbt = stack.getTagCompound();
		// This is the suggested key under which to store item component data.
		// You are free to change this as you please.
		if (!nbt.hasKey("oc:data")) {
			nbt.setTag("oc:data", new NBTTagCompound());
		}
		return nbt.getCompoundTag("oc:data");
	}

	@Override
	public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host) {
		return worksWith(stack) && Rack.class.isAssignableFrom(host);
	}

}

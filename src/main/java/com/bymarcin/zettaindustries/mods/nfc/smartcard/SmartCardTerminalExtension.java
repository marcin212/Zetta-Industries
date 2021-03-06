package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.ZettaIndustries;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.item.Container;
import li.cil.oc.api.driver.item.HostAware;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;

import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SmartCardTerminalExtension extends Item implements HostAware, DriverItem, Container{

	public SmartCardTerminalExtension() {
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
	}
	
	@Override
	public String providedSlot(ItemStack stack) {
		return Slot.Any;
	}

	@Override
	public int providedTier(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean worksWith(ItemStack stack) {
		return stack.getItem().equals(this);
	}

	@Override
	public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
		return new SCE(host);
	}

	@Override
	public String slot(ItemStack stack) {
		return Slot.Container;
	}

	@Override
	public int tier(ItemStack stack) {
		return 1;
	}

	@Override
	public NBTTagCompound dataTag(ItemStack stack) {
		return stack.hasTagCompound()?stack.getTagCompound():new NBTTagCompound();
	}

	@Override
	public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host) {
		return worksWith(stack);
	}

	public static class SCE extends AbstractManagedEnvironment {
		
		EnvironmentHost host;
		
		public SCE(EnvironmentHost host){
			this.host = host;
			setNode(Network.newNode(this, Visibility.Network).withConnector().withComponent("sce", Visibility.Network).create());
		}
		
		@Override
		public boolean canUpdate() {
			return true;
		}
		
		@Override
		public void update() {
			//((Tablet)host).internalComponents().forEach( (x) -> System.out.println(x.getItem()) );
			super.update();
		}
	}
}

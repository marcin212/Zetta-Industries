package com.bymarcin.zettaindustries.mods.eawiring.connectors.wires;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.EAWiring;
import com.bymarcin.zettaindustries.mods.eawiring.MaterialResistivity;
import com.bymarcin.zettaindustries.mods.eawiring.connection.WireProperties;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.items.ItemEAWireCoil;

import net.minecraft.item.ItemStack;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;

public class HVWire extends WireBase {

	public HVWire() {
		super();
		wireProperties = new WireProperties(212,760,220000,MaterialResistivity.getMaterialResistivityPerBlock(MaterialResistivity.TUNGSTEN, 0.025));

	}

	@Override
	public int getColour(Connection arg0) {
		return 5460819;
	}

	@Override
	public int getMaxLength() {
		return 64;
	}

	@Override
	public double getRenderDiameter() {
		return .0625;
	}

	@Override
	public String getUniqueName() {
		return ZettaIndustries.MODID + ":EAWireHV";
	}

	@Override
	public ItemStack getWireCoil() {
		return new ItemStack(EAWiring.wires, 1, ItemEAWireCoil.coil_tungsten);
	}
}

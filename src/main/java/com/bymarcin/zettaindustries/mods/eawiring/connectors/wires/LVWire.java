package com.bymarcin.zettaindustries.mods.eawiring.connectors.wires;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.EAWiring;
import com.bymarcin.zettaindustries.mods.eawiring.MaterialResistivity;
import com.bymarcin.zettaindustries.mods.eawiring.connection.WireProperties;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.items.ItemEAWireCoil;

import net.minecraft.item.ItemStack;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;

public class LVWire extends WireBase {

	public LVWire() {
		super();
		wireProperties = new WireProperties(212,150,10000,MaterialResistivity.getMaterialResistivityPerBlock(MaterialResistivity.COPPER, 0.008));
	}

	@Override
	public int getColour(Connection arg0) {
		return 10969895;
	}

	@Override
	public int getMaxLength() {
		return 64;
	}

	@Override
	public double getRenderDiameter() {
		return .0425;
	}

	@Override
	public String getUniqueName() {
		return ZettaIndustries.MODID + ":EAWireLV";
	}

	@Override
	public ItemStack getWireCoil() {
		return new ItemStack(EAWiring.wires, 1, ItemEAWireCoil.coil_copper);
	}

}

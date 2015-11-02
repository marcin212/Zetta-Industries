package com.bymarcin.zettaindustries.mods.eawiring.connectors.wires;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.EAWiring;
import com.bymarcin.zettaindustries.mods.eawiring.MaterialResistivity;
import com.bymarcin.zettaindustries.mods.eawiring.connection.WireProperties;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.items.ItemEAWireCoil;

import net.minecraft.item.ItemStack;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;

public class MVWire extends WireBase {
	public MVWire() {
		super();
		wireProperties = new WireProperties(212,450,110000,MaterialResistivity.getMaterialResistivityPerBlock(MaterialResistivity.IRON, 0.017));
	}

	@Override
	public int getColour(Connection arg0) {
		return 6842472;
	}

	@Override
	public int getMaxLength() {
		return 64;
	}

	@Override
	public double getRenderDiameter() {
		return .0525;
	}

	@Override
	public String getUniqueName() {
		return ZettaIndustries.MODID + ":EAWireMV";
	}

	@Override
	public ItemStack getWireCoil() {
		return new ItemStack(EAWiring.wires, 1, ItemEAWireCoil.coil_iron);
	}

}

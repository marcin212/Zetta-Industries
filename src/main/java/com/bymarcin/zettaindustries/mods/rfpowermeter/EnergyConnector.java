package com.bymarcin.zettaindustries.mods.rfpowermeter;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Created by Marcin on 2017-06-12.
 */
public class EnergyConnector implements IEnergyStorage {
	private IEnergyMachine owner;
	private EnumFacing dir;

	public EnergyConnector(IEnergyMachine te, EnumFacing dir) {
		this.owner = te;
		this.dir = dir;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return owner.receiveEnergy(maxReceive, dir, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return owner.receiveEnergy(maxExtract, dir, simulate);
	}

	@Override
	public int getEnergyStored() {
		return owner.getEnergyStored(dir);
	}

	@Override
	public int getMaxEnergyStored() {
		return owner.getMaxEnergyStored(dir);
	}

	@Override
	public boolean canExtract() {
		return owner.canExtract(dir);
	}

	@Override
	public boolean canReceive() {
		return owner.canReceive(dir);
	}
}

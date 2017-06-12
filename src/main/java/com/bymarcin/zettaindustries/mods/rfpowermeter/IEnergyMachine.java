package com.bymarcin.zettaindustries.mods.rfpowermeter;

import net.minecraft.util.EnumFacing;

/**
 * Created by Marcin on 2017-06-12.
 */
public interface IEnergyMachine {
	int receiveEnergy(int maxReceive, EnumFacing dir, boolean simulate);
	int extractEnergy(int maxExtract, EnumFacing dir, boolean simulate);
	int getEnergyStored(EnumFacing dir);
	int getMaxEnergyStored(EnumFacing dir);
	boolean canExtract(EnumFacing dir);
	boolean canReceive(EnumFacing dir);
}

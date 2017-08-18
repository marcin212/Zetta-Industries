package com.bymarcin.zettaindustries.mods.battery;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class AdvancedStorage implements IEnergyStorage {

	protected long energy;
	protected long capacity;
	protected int maxReceive;
	protected int maxExtract;

	public AdvancedStorage(long capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer);
	}

	public AdvancedStorage(long capacity, int maxReceive, int maxExtract) {
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public void merge(AdvancedStorage other){
		if(other.energy > energy){
			energy = other.energy;
			capacity = other.capacity;
		}
	}
	
	
	public AdvancedStorage readFromNBT(NBTTagCompound nbt) {

		this.energy = nbt.getLong("Energy");

//		if (energy > capacity) {
//			energy = capacity;
//		}

		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

//		if (energy < 0) {
//			energy = 0;
//		}
		nbt.setLong("Energy", energy);
		return nbt;
	}

	public void setCapacity(long capacity) {

		this.capacity = capacity;

		if (energy > capacity) {
			energy = capacity;
		}
	}

	public void setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
	}

	public int getMaxReceive() {

		return maxReceive;
	}

	public int getMaxExtract() {

		return maxExtract;
	}

	/**
	 * This function is included to allow for server -> client sync. Do not call this externally to the containing Tile Entity, as not all IEnergyHandlers are
	 * guaranteed to have it.
	 * 
	 * @param energy
	 */
	public void setEnergyStored(long energy) {

		this.energy = energy;

		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	/**
	 * This function is included to allow the containing tile to directly and efficiently modify the energy contained in the EnergyStorage. Do not rely on this
	 * externally, as not all IEnergyHandlers are guaranteed to have it.
	 * 
	 * @param energy
	 */
	public void modifyEnergyStored(int energy) {

		this.energy += energy;

		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	/* IEnergyStorage */
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {

		int energyReceived = (int) Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {

		int energyExtracted = (int) Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return (energy <= Integer.MAX_VALUE)?(int) energy:Integer.MAX_VALUE;
	}

	@Override
	public int getMaxEnergyStored() {
		return (capacity <= Integer.MAX_VALUE)?(int) capacity:Integer.MAX_VALUE;
	}

	@Override
	public boolean canExtract() {
		return getMaxExtract() > 0;
	}

	@Override
	public boolean canReceive() {
		return getMaxReceive() > 0;
	}

	public long getRealEnergyStored() {
		return energy;
	}

	public long getRealMaxEnergyStored() {
		return capacity;
	}
	
	
	
	
	
}

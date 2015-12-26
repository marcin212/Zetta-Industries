package com.bymarcin.zettaindustries.mods.mgc.tileentities;

import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricPole;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.electricity.ITileElectricPole;
import com.cout970.magneticraft.api.electricity.prefab.ElectricConductor;
import com.cout970.magneticraft.api.electricity.prefab.ElectricPoleTier1;
import com.cout970.magneticraft.api.util.VecInt;
import com.cout970.magneticraft.api.util.VecDouble;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class ElectricalConnectorTileEntity extends TileEntity implements ITileElectricPole, IElectricTile{
	IElectricConductor input  =  new ElectricConductor(this,0, 0.001);
	IElectricPole component = new ElectricPoleTier1(this, input){	
		public VecDouble[] getWireConnectors() {
			return new VecDouble[]{new VecDouble(0.5, 0.5, 0.5)};
		};
	};
	
	
	@Override
	public void updateEntity() {
		input.recache();
		input.iterate();
		component.iterate();
		
	}
	
	@Override
	public ITileElectricPole getMainTile() {
		return this;
	}
	
	@Override
	public IElectricPole getPoleConnection() {
		return component;
	}

	@Override
	public IElectricConductor[] getConds(VecInt arg0, int arg1) {
		return new IElectricConductor[]{input};
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		input.save(nbt);
		component.save(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		input.load(nbt);
		component.load(nbt);
	}
	
}

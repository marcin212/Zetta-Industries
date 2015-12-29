package com.bymarcin.zettaindustries.mods.mgc.tileentities;

import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.electricity.prefab.ElectricConductor;
import com.cout970.magneticraft.api.util.VecInt;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameData;

public class ElectricalCoveredTileEntity extends TileEntity implements IElectricTile{
	public String blockID = "";
	public IIcon icon;
	public Block block;
	IElectricConductor condT0 = new ElectricConductor(this, 0, 0.0001);
	IElectricConductor condT2 = new ElectricConductor(this, 2, 0.0001);
	IElectricConductor condT4 = new ElectricConductor(this, 4, 0.0001);

	@Override
	public void updateEntity() {
		super.updateEntity();
		condT0.recache();
		condT0.iterate();
		condT2.recache();
		condT2.iterate();
		condT4.recache();
		condT4.iterate();
	}
	
	@Override
	public IElectricConductor[] getConds(VecInt vec, int tier) {
		switch(tier){
		case 0:
			return new IElectricConductor[]{condT0};
		case 2:
			return new IElectricConductor[]{condT2};
		case 4:
			return new IElectricConductor[]{condT4};
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound t0 = new NBTTagCompound();
		NBTTagCompound t2 = new NBTTagCompound();
		NBTTagCompound t4 = new NBTTagCompound();
		condT0.save(t0);
		condT2.save(t2);
		condT4.save(t4);
		nbt.setTag("T0", t0);
		nbt.setTag("T2", t2);
		nbt.setTag("T4", t4);
		nbt.setString("BlockInfo", GameData.getBlockRegistry().getNameForObject(block));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagCompound t0 = nbt.getCompoundTag("T0");
		NBTTagCompound t2 = nbt.getCompoundTag("T2");
		NBTTagCompound t4 = nbt.getCompoundTag("T4");
		condT0.load(t0);
		condT2.load(t2);
		condT4.load(t4);
		block =  GameData.getBlockRegistry().getObject(nbt.getString("BlockInfo"));
	}
	
}

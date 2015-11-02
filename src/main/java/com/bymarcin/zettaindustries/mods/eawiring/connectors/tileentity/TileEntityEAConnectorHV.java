package com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity;

import com.bymarcin.zettaindustries.mods.eawiring.connectors.wires.WireBase;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;

public class TileEntityEAConnectorHV extends TileEntityConnectorBase{

	@Override
	public boolean canConnectCable(WireType wt, TargetingInfo arg1) {
		return wt==WireBase.HVWire;
	}

	@Override
	public WireType getCableLimiter(TargetingInfo arg0) {
		return WireBase.HVWire;
	}

	@Override
	public Vec3 getRaytraceOffset(IImmersiveConnectable link)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		return Vec3.createVectorHelper(.5+fd.offsetX*.3125, .5+fd.offsetY*.3125, .5+fd.offsetZ*.3125);
	}
	@Override
	public Vec3 getConnectionOffset(Connection con)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		double conRadius = con.cableType.getRenderDiameter()/2;
		return Vec3.createVectorHelper(.5+fd.offsetX*(.25-conRadius), .5+fd.offsetY*(.25-conRadius), .5+fd.offsetZ*(.25-conRadius));
	}

	@Override
	public ForgeDirection[] getValidEAConnectionSide() {
		return new ForgeDirection[0];
	}

}

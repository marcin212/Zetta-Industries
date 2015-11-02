package com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity;

import com.bymarcin.zettaindustries.mods.eawiring.connectors.wires.WireBase;


import net.minecraft.util.Vec3;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;

public class TileEntityEAConnectorMV  extends TileEntityConnectorBase{

	@Override
	public Vec3 getRaytraceOffset(IImmersiveConnectable link)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		return Vec3.createVectorHelper(.5+fd.offsetX*.125, .5+fd.offsetY*.125, .5+fd.offsetZ*.125);
	}
	@Override
	public Vec3 getConnectionOffset(Connection con)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		double conRadius = con.cableType.getRenderDiameter()/2;
		return Vec3.createVectorHelper(.5+fd.offsetX*(.0625-conRadius), .5+fd.offsetY*(.0625-conRadius), .5+fd.offsetZ*(.0625-conRadius));
	}
	
	@Override
	public boolean canConnectCable(WireType wt, TargetingInfo arg1) {
		return wt==WireBase.MVWire;
	}

	@Override
	public WireType getCableLimiter(TargetingInfo arg0) {
		return WireBase.MVWire;
	}
	
	@Override
	public ForgeDirection[] getValidEAConnectionSide() {
		return new ForgeDirection[0];
	}
}

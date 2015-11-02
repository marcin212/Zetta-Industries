package com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity;

import com.bymarcin.zettaindustries.mods.eawiring.connectors.wires.WireBase;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;

public class TileEntityEAConnectorRHV  extends TileEntityConnectorBase{
	
	@Override
	public Vec3 getRaytraceOffset(IImmersiveConnectable link)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		return Vec3.createVectorHelper(.5+fd.offsetX*.4375, .5+fd.offsetY*.4375, .5+fd.offsetZ*.4375);
	}

	@Override
	public Vec3 getConnectionOffset(Connection con)
	{
		double conRadius = con.cableType.getRenderDiameter()/2;
		return Vec3.createVectorHelper(.5, .125+conRadius, .5);
	}

	@Override
	public boolean canConnectCable(WireType wt, TargetingInfo arg1) {
		return wt==WireBase.HVWire;
	}

	@Override
	public WireType getCableLimiter(TargetingInfo arg0) {
		return WireBase.HVWire;
	}
	
	@Override
	public void setFacing(int facing) {
		this.facing = ForgeDirection.UP.ordinal();
	}
	
	@Override
	public void setQFacing(int qFacing) {
		
	}

	@Override
	public ForgeDirection[] getValidEAConnectionSide() {
		return new ForgeDirection[0];
	}
}

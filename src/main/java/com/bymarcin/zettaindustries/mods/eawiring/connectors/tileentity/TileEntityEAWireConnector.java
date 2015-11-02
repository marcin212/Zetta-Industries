package com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity;

import com.bymarcin.zettaindustries.registry.ICustomHighlight;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;

public class TileEntityEAWireConnector  extends TileEntityConnectorBase{
	private WireType limit = null;


	@Override 
	public Vec3 getRaytraceOffset(IImmersiveConnectable arg0) {
		ForgeDirection fd = ForgeDirection.UP;
		return Vec3.createVectorHelper(.5 , 13/16f, .5);
	}

	@Override
	public Vec3 getConnectionOffset(Connection con)
	{
		ForgeDirection fd = ForgeDirection.UP;
		return Vec3.createVectorHelper(.5, 15/16f, .5);
	}

	@Override
	public boolean canConnectCable(WireType wt, TargetingInfo arg1) {
		return limit==null || wt==limit;
	}
	
	@Override
	public void connectCable(WireType wt, TargetingInfo arg1) {
		limit = wt;
		super.connectCable(wt, arg1);
	}
	
	@Override
	public void removeCable(Connection con) {
		limit=null;
		super.removeCable(con);
	}

	@Override
	public WireType getCableLimiter(TargetingInfo target) {
		return limit;
	}

	@Override
	public ForgeDirection[] getValidEAConnectionSide() {
		return new ForgeDirection[]{Q_VALID_DIRECTIONS[qFacing]};
	}

}

package com.bymarcin.zettaindustries.mods.eawiring.connectors.node;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.connection.AbstractConnection;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityConnectorBase;
import com.sun.org.apache.bcel.internal.generic.NEW;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import mods.eln.Eln;
import mods.eln.misc.Direction;
import mods.eln.misc.LRDU;
import mods.eln.node.simple.SimpleNode;
import mods.eln.sim.ElectricalLoad;
import mods.eln.sim.ThermalLoad;
import mods.eln.sim.nbt.NbtElectricalLoad;

public class ConnectorNode extends SimpleNode{
	NbtElectricalLoad connectionPoint = new NbtElectricalLoad("connectionPoint");
	
	@Override
	public void initialize() {
		connectionPoint.setRs(1E-9);
		electricalLoadList.add(connectionPoint);
		connect();
	}

	@Override
	public String getNodeUuid() {
		return getNodeID();
	}
	
	public ElectricalLoad getConnectionPoint(){	
		return connectionPoint;
	}

	public static String getNodeID() {
		return ZettaIndustries.MODID+":ConnectorNode";
	}
	
	@Override
	public String thermoMeterString(Direction side) {
		ChunkCoordinates myCoords = new ChunkCoordinates(coordonate.x, coordonate.y, coordonate.z);
		Set<Connection>  connections = ImmersiveNetHandler.INSTANCE.getConnections(coordonate.world(), myCoords);

		StringBuilder sb = new StringBuilder();
		if(connections==null) return "";
		boolean ownCheck = false;
		
		for(Connection c: connections){
			TileEntity myTE = null;
			TileEntity otherTE = null;
			
			if(c.start.equals(myCoords)){
				myTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				otherTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			}else {
				otherTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				myTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			}

			if(!ownCheck){
				ownCheck = true;
				for(AbstractConnection con : ((TileEntityConnectorBase)myTE).getConnectionsForPoint(coordonate)){
					sb.append(" [" + con.getThermalInfo()+"]\n");
				}
			}
			
			for(AbstractConnection con : ((TileEntityConnectorBase)otherTE).getConnectionsForPoint(coordonate)){
				sb.append(" [" + con.getThermalInfo()+"]\n");
			}
		}
		
		return sb.toString();
	}
	
	@Override
	public String multiMeterString(Direction side) {
		ChunkCoordinates myCoords = new ChunkCoordinates(coordonate.x, coordonate.y, coordonate.z);
		Set<Connection>  connections = ImmersiveNetHandler.INSTANCE.getConnections(coordonate.world(), myCoords);

		StringBuilder sb = new StringBuilder();
		if(connections==null) return "";
		boolean ownCheck = false;
		
		for(Connection c: connections){
			TileEntity myTE = null;
			TileEntity otherTE = null;
			
			if(c.start.equals(myCoords)){
				myTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				otherTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			}else {
				otherTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				myTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			}

			if(!ownCheck){
				ownCheck = true;
				for(AbstractConnection con : ((TileEntityConnectorBase)myTE).getConnectionsForPoint(coordonate)){
					sb.append(" [" + con.getVoltometrInfo()+"]\n");
				}
			}
			
			for(AbstractConnection con : ((TileEntityConnectorBase)otherTE).getConnectionsForPoint(coordonate)){
				sb.append(" [" + con.getVoltometrInfo()+"]\n");
			}
		}
		
		return sb.toString();
	}
	
	@Override
	public int getSideConnectionMask(Direction directionA, LRDU lrduA) {
		TileEntityConnectorBase te = ((TileEntityConnectorBase)coordonate.getTileEntity());
		if(te == null) return 0;
		ForgeDirection[] sides = te.getValidEAConnectionSide();
		return sides.length==0?0:sides[0].equals(directionA.toForge())?maskElectricalPower:0;
	}

	@Override
	public ThermalLoad getThermalLoad(Direction directionA, LRDU lrduA) {
		return null;
	}

	@Override
	public ElectricalLoad getElectricalLoad(Direction directionB, LRDU lrduB) {
		ForgeDirection[] sides = ((TileEntityConnectorBase)coordonate.getTileEntity()).getValidEAConnectionSide();
		return sides.length==0?null:sides[0].equals(directionB.toForge())?connectionPoint:null;
	}

}

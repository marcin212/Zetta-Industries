package com.bymarcin.zettaindustries.mods.eawiring.connection;


import com.bymarcin.zettaindustries.mods.eawiring.connectors.node.ConnectorNode;

import mods.eln.Eln;
import mods.eln.misc.Coordonate;
import mods.eln.misc.INBTTReady;
import mods.eln.misc.Utils;
import mods.eln.node.NodeBase;
import mods.eln.node.NodeManager;
import mods.eln.sim.nbt.NbtThermalLoad;

import net.minecraft.nbt.NBTTagCompound;

public class AbstractConnection implements INBTTReady{
	Coordonate node1;
	Coordonate node2;
	NbtThermalLoad thermalLoad = new NbtThermalLoad("thermalLoad");
	AbstractConnectionWatchDog heater;
	WireProperties wire = new WireProperties();
	VirtualElectricalConnection vec;
	boolean isAdded = false;
	
	public AbstractConnection(){

	}
	
	public String getThermalInfo(){
		return Utils.plotCelsius("Tc",thermalLoad.Tc);
	}
	
	public String getVoltometrInfo(){
		return Utils.plotUIP(vec.getU(), vec.getI()) + Utils.plotOhm("R", vec.getR());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AbstractConnection)) return false;
		AbstractConnection con = (AbstractConnection) obj;
		return (con.node1.equals(node1) && con.node2.equals(node2)) || (con.node1.equals(node2) && con.node2.equals(node1));
	}
	
	public AbstractConnection(ConnectorNode node1, ConnectorNode node2, WireProperties wire) {
		this.wire = wire;
		this.node1 = node1.coordonate;
		this.node2 = node2.coordonate;
	}
	
	public Coordonate getCoords1(){
		return node1;
	}
	
	public Coordonate getCoords2(){
		return node2;
	}
	
	public void init(){
		if(isAdded) return;
		double distance = calculateDistance(node1, node2);
		
		if(vec==null){
			vec = new VirtualElectricalConnection(getNode1().getConnectionPoint(), getNode2().getConnectionPoint());
		}
		
		if(heater == null){
			heater =  new AbstractConnectionWatchDog(vec, thermalLoad, wire.thermalWarmLimit, node1, node2);
		}
			
		wire.setDistance(distance);
		thermalLoad.set(wire.getThermalRs(), wire.getThermalRp(), wire.getThermalC());
		vec.setR(calculateDistance(node1, node2)*wire.getResistancePerBlock());
		Eln.simulator.addElectricalComponent(vec);
		Eln.simulator.addThermalLoad(thermalLoad);
		Eln.simulator.addSlowProcess(heater);
		isAdded = true;
	}
	
	public void onRemove(){
		vec.onRemovefromRootSystem();
		Eln.simulator.removeElectricalComponent(vec);
		Eln.simulator.removeThermalLoad(thermalLoad);
		Eln.simulator.removeSlowProcess(heater);
		isAdded = false;
	}

	public ConnectorNode getNode1(){
		if(node1==null) return null;
		NodeBase n = NodeManager.instance.getNodeFromCoordonate(node1);
		if(n instanceof ConnectorNode)
			return (ConnectorNode) n;
		return null;
	}
	
	public ConnectorNode getNode2(){
		if(node2==null) return null;
		NodeBase n = NodeManager.instance.getNodeFromCoordonate(node2);
		if(n instanceof ConnectorNode)
			return (ConnectorNode) n;
		return null;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt, String str) {
		node1 = new Coordonate();
		node2 = new Coordonate();
		node1.readFromNBT(nbt, str+"-node1");
		node2.readFromNBT(nbt, str+"-node2");
		thermalLoad.readFromNBT(nbt, str+"-thermalLoad");
		wire.readFromNBT(nbt, str+"-wire");

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String str) {
		node1.writeToNBT(nbt, str+"-node1");
		node2.writeToNBT(nbt, str+"-node2");
		wire.writeToNBT(nbt, str+"-wire");
		thermalLoad.writeToNBT(nbt, str+"-thermalLoad");
	}
	
	public static double calculateDistance(Coordonate point1, Coordonate point2){
		double xd = point2.x - point1.x;
		double yd = point2.y - point1.y;
		double zd = point2.z - point1.z;
		double result = Math.ceil(Math.sqrt(xd*xd + yd*yd + zd*zd));
		return result;
	}
	
}

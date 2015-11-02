package com.bymarcin.zettaindustries.mods.eawiring.mosfet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mods.eln.misc.Direction;
import mods.eln.misc.LRDU;
import mods.eln.misc.Utils;
import mods.eln.node.simple.SimpleNode;
import mods.eln.sim.ElectricalLoad;
import mods.eln.sim.IProcess;
import mods.eln.sim.ThermalLoad;
import mods.eln.sim.mna.component.Resistor;
import mods.eln.sim.mna.misc.MnaConst;
import mods.eln.sim.nbt.NbtElectricalGateInput;
import mods.eln.sim.nbt.NbtElectricalLoad;
import mods.eln.sim.nbt.NbtResistor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.util.ForgeDirection;

public class MosfetNode extends SimpleNode{
	NbtElectricalLoad loadS = new NbtElectricalLoad("loadS");
	NbtElectricalLoad loadG = new NbtElectricalLoad("loadG");
	NbtElectricalGateInput gate = new NbtElectricalGateInput("gate");
	Resistor switchResistor = new Resistor(loadG, loadS);
	
	
	public static class MosfetProcess implements IProcess{
		Resistor switchResistor;
		NbtElectricalGateInput gate;
		double Ut=1.8;
		double minR = 0.0037;
		NbtElectricalLoad source;
		NbtElectricalLoad drain;
		public MosfetProcess(NbtElectricalGateInput gate, Resistor switchResistor, NbtElectricalLoad loadS, NbtElectricalLoad loadG){
			this.gate = gate;
			this.switchResistor = switchResistor;
			this.source = loadS;
			this.drain = loadG;
		}

		private double Ugs(){
			return gate.getU()-source.getU();
		}
		
		private boolean isActive(){
			return (drain.getU() - source.getU())>0;
		}
		
		@Override
		public void process(double time) {
			double r = switchResistor.getR();
			if(Ugs()<=Ut){
				r = MnaConst.ultraImpedance;
			}else if(Ugs()<100+Ut){
				r = (1/((Ugs()-Ut)*(Ugs()-Ut)))/1000 + minR;
			}else{
				r = minR;
			}
			
			r = isActive()?r:MnaConst.ultraImpedance;
		
			if(r!=switchResistor.getR()){
				switchResistor.setR(r);
			}
		}

	}
	
	@Override
	public void initialize() {
		
		electricalLoadList.add(loadS);
		loadS.setRs(1E-9);
		electricalLoadList.add(loadG);
		loadG.setRs(1E-9);
		
		
		
		switchResistor.setR(100);
		electricalComponentList.add(switchResistor);
		electricalLoadList.add(gate);
		slowProcessList.add(new MosfetProcess(gate, switchResistor, loadS, loadG));
		//switchResistor.setR(100);  Utils.plotOhm("R", switchResistor.getR()) + Utils.plotUIP(switchResistor.getU(), switchResistor.getCurrent())
		
		connect();
		needPublish();
		
	}

	@Override
	public String multiMeterString(Direction side) {
		return Utils.plotOhm("R", switchResistor.getR()) + Utils.plotVolt("Ugs", gate.getU()-loadS.getU());
	}

	@Override
	public String getNodeUuid() {
		return getNodeUuidStatic();
	}

	int[][] ROTATION_MATRIX = new int[][]{
			{0,3,1,2},
			{1,2,0,3},
			{2,0,3,1},
			{3,1,2,0}
	};
	
	@Override
	public int getSideConnectionMask(Direction directionA, LRDU lrduA) {
		int f = getFace();
		System.out.println("DIR:" + directionA.toForge() + "|" + getFace() + "|||" + directionA.toForge().ordinal() +"/" + (ForgeDirection.WEST.ordinal()+f));
		if(directionA.toForge().ordinal()-2 == ROTATION_MATRIX[ForgeDirection.SOUTH.ordinal()-2][f] || directionA.toForge().ordinal()-2 == ROTATION_MATRIX[ForgeDirection.NORTH.ordinal()-2][f]){
			return maskElectricalPower;
		}
		if(directionA.toForge().ordinal()-2 == ROTATION_MATRIX[ForgeDirection.WEST.ordinal()-2][f]){
			return maskElectricalInputGate;
		}
		return 0;
	}

	@Override
	public ThermalLoad getThermalLoad(Direction directionA, LRDU lrduA) {
		return null;
	}

	@Override
	public ElectricalLoad getElectricalLoad(Direction directionB, LRDU lrduB) {
		int f = getFace();
		if(directionB.toForge().ordinal()-2 == ROTATION_MATRIX[ForgeDirection.SOUTH.ordinal()-2][f]){
			return loadS;
		}
		if(directionB.toForge().ordinal()-2 == ROTATION_MATRIX[ForgeDirection.NORTH.ordinal()-2][f]){
			return loadG;
		}
		if(directionB.toForge().ordinal()-2 ==ROTATION_MATRIX[ForgeDirection.WEST.ordinal()-2][f]){
			return gate;
		}
		return null;
	}
	
    public static String getNodeUuidStatic() {
		return "Mosfet";
	}
    
    public int getFace(){
    	TileEntity te = coordonate.world().getTileEntity(coordonate.x, coordonate.y, coordonate.z);
    	if(te instanceof TEMosfet){
    		return ((TEMosfet) te).face;
    	}
    	return 0;
    }
    
    public void setFace(int face){
    	TileEntity te = coordonate.world().getTileEntity(coordonate.x, coordonate.y, coordonate.z);
    	if(te instanceof TEMosfet){
    		((TEMosfet) te).setFace(face);
    	}
    }
	
}

package com.bymarcin.zettaindustries.mods.eawiring.connectors.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.connection.AbstractConnection;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.block.EAConnector;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityConnectorBase;
import com.sun.org.apache.bcel.internal.generic.NEW;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
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
import mods.eln.sim.IProcess;
import mods.eln.sim.ThermalLoad;
import mods.eln.sim.nbt.NbtElectricalLoad;
import mods.eln.sim.nbt.NbtResistor;
import mods.eln.sim.process.destruct.VoltageStateWatchDog;
import mods.eln.sim.process.destruct.WorldExplosion;

public class ConnectorNode extends SimpleNode {
	NbtElectricalLoad connectionPoint = new NbtElectricalLoad("connectionPoint");
	int type;
	@Override
	public void initialize() {
		
		
		connectionPoint.setRs(1E-9);
		electricalLoadList.add(connectionPoint);


		if (type == EAConnector.connectorConventer) {
			powerInResistor = new NbtResistor("powerInResistor", connectionPoint, null);
			electricalComponentList.add(powerInResistor);
			electricalProcessList.add(electricalProcess);
			slowProcessList.add(watchdog);
			WorldExplosion exp = new WorldExplosion(coordonate).machineExplosion();
			watchdog.set(connectionPoint).setUNominal(inStdVoltage).set(exp);
		}

		connect();
	}
	
	
	public void initializeFromThat(Direction front, net.minecraft.entity.EntityLivingBase entityLiving, net.minecraft.item.ItemStack itemStack) {
		type = itemStack.getItemDamage();
		super.initializeFromThat(front, entityLiving, itemStack);
	};
	

	NbtResistor powerInResistor;
	ElectricalProcess electricalProcess = new ElectricalProcess();
	VoltageStateWatchDog watchdog = new VoltageStateWatchDog();

	public double energyBuffer = 0;
	public static double energyBufferMax = 10000;
	public static double inStdVoltage = 800;
	public static double inPowerMax = 10000;

	public double inPowerFactor = 0.5;

	public static final byte setInPowerFactor = 1;

	class ElectricalProcess implements IProcess {
		double timeout = 0;

		@Override
		public void process(double time) {
			energyBuffer += powerInResistor.getP() * time;
			timeout -= time;
			if (timeout < 0) {
				timeout = 0.05;
				double energyMiss = energyBufferMax - energyBuffer;
				if (energyMiss <= 0) {
					powerInResistor.highImpedance();
				} else {
					double factor = Math.min(1, energyMiss / energyBufferMax * 2);
					if (factor < 0.005)
						factor = 0;
					double inP = factor * inPowerMax * inPowerFactor;
					powerInResistor.setR(inStdVoltage * inStdVoltage / inP);
				}
			}
		}
	}

	public double getOtherModEnergyBuffer(double conversionRatio) {
		return energyBuffer * conversionRatio;
	}

	public void drawEnergy(double otherModEnergy, double conversionRatio) {
		energyBuffer -= otherModEnergy / conversionRatio;
	}

	public double getOtherModOutMax(double otherOutMax, double conversionRatio) {
		return Math.min(getOtherModEnergyBuffer(conversionRatio), otherOutMax);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setDouble("energyBuffer", energyBuffer);
		nbt.setDouble("inPowerFactor", inPowerFactor);
		nbt.setInteger("typeConnector", type);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		energyBuffer = nbt.getDouble("energyBuffer");
		inPowerFactor = nbt.getDouble("inPowerFactor");
		type = nbt.getInteger("typeConnector");
	}

	@Override
	public boolean hasGui(Direction side) {
		return true;
	}

	@Override
	public void publishSerialize(DataOutputStream stream) {
		super.publishSerialize(stream);

		try {
			stream.writeFloat((float) inPowerFactor);
			stream.writeFloat((float) inPowerMax);
			stream.writeInt((int) inStdVoltage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void networkUnserialize(DataInputStream stream, EntityPlayerMP player) {
		try {
			switch (stream.readByte()) {
			case setInPowerFactor:
				inPowerFactor = stream.readFloat();
				needPublish();
				break;

			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getNodeUuid() {
		return getNodeID();
	}

	public ElectricalLoad getConnectionPoint() {
		return connectionPoint;
	}

	public static String getNodeID() {
		return ZettaIndustries.MODID + ":ConnectorNode";
	}

	@Override
	public String thermoMeterString(Direction side) {
		ChunkCoordinates myCoords = new ChunkCoordinates(coordonate.x, coordonate.y, coordonate.z);
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(coordonate.world(), myCoords);

		StringBuilder sb = new StringBuilder();
		if (connections == null)
			return "";
		boolean ownCheck = false;

		for (Connection c : connections) {
			TileEntity myTE = null;
			TileEntity otherTE = null;

			if (c.start.equals(myCoords)) {
				myTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				otherTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			} else {
				otherTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				myTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			}

			if (!ownCheck) {
				ownCheck = true;
				for (AbstractConnection con : ((TileEntityConnectorBase) myTE).getConnectionsForPoint(coordonate)) {
					sb.append(" [" + con.getThermalInfo() + "]\n");
				}
			}

			for (AbstractConnection con : ((TileEntityConnectorBase) otherTE).getConnectionsForPoint(coordonate)) {
				sb.append(" [" + con.getThermalInfo() + "]\n");
			}
		}

		return sb.toString();
	}

	@Override
	public String multiMeterString(Direction side) {
		ChunkCoordinates myCoords = new ChunkCoordinates(coordonate.x, coordonate.y, coordonate.z);
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(coordonate.world(), myCoords);

		StringBuilder sb = new StringBuilder();
		if (connections == null)
			return "";
		boolean ownCheck = false;

		for (Connection c : connections) {
			TileEntity myTE = null;
			TileEntity otherTE = null;

			if (c.start.equals(myCoords)) {
				myTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				otherTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			} else {
				otherTE = coordonate.world().getTileEntity(c.start.posX, c.start.posY, c.start.posZ);
				myTE = coordonate.world().getTileEntity(c.end.posX, c.end.posY, c.end.posZ);
			}

			if (!ownCheck) {
				ownCheck = true;
				for (AbstractConnection con : ((TileEntityConnectorBase) myTE).getConnectionsForPoint(coordonate)) {
					sb.append(" [" + con.getVoltometrInfo() + "]\n");
				}
			}

			for (AbstractConnection con : ((TileEntityConnectorBase) otherTE).getConnectionsForPoint(coordonate)) {
				sb.append(" [" + con.getVoltometrInfo() + "]\n");
			}
		}

		return sb.toString();
	}

	@Override
	public int getSideConnectionMask(Direction directionA, LRDU lrduA) {
		TileEntityConnectorBase te = ((TileEntityConnectorBase) coordonate.getTileEntity());
		if (te == null)
			return 0;
		ForgeDirection[] sides = te.getValidEAConnectionSide();
		return sides.length == 0 ? 0 : sides[0].equals(directionA.toForge()) ? maskElectricalPower : 0;
	}

	@Override
	public ThermalLoad getThermalLoad(Direction directionA, LRDU lrduA) {
		return null;
	}

	@Override
	public ElectricalLoad getElectricalLoad(Direction directionB, LRDU lrduB) {
		ForgeDirection[] sides = ((TileEntityConnectorBase) coordonate.getTileEntity()).getValidEAConnectionSide();
		return sides.length == 0 ? null : sides[0].equals(directionB.toForge()) ? connectionPoint : null;
	}

}

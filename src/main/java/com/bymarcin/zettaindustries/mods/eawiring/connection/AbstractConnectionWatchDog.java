package com.bymarcin.zettaindustries.mods.eawiring.connection;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import mods.eln.misc.Coordonate;
import mods.eln.sim.IProcess;
import mods.eln.sim.ThermalLoad;

public class AbstractConnectionWatchDog implements IProcess {
	VirtualElectricalConnection connection;
	ThermalLoad thermalLoad;
	double warmLimit;

	
	ChunkCoordinates from;
	ChunkCoordinates to;
	World world;

	
	public AbstractConnectionWatchDog(VirtualElectricalConnection connection, ThermalLoad thermalLoad, double warmLimit, Coordonate from, Coordonate to) {
		this.thermalLoad = thermalLoad;
		this.connection = connection;
		this.warmLimit = warmLimit;
		this.world = from.world();
		this.from = new ChunkCoordinates(from.x, from.y, from.z);
		this.to = new ChunkCoordinates(to.x, to.y, to.z);
	}
	
	@Override
	public void process(double time) {
		double I = connection.getI();
		if(thermalLoad.getT()>warmLimit){	
			Set<Connection>  connections = ImmersiveNetHandler.INSTANCE.getConnections(world, from);
			for(Connection con : connections){
				if( (con.start.equals(from) && con.end.equals(to)) ||  (con.start.equals(to) && con.end.equals(from)) ){
					ImmersiveNetHandler.INSTANCE.getTransferedRates(world.provider.dimensionId).put(con, Integer.MAX_VALUE);
					break;
				}
			}
			
		}
		thermalLoad.movePowerTo(I * I * connection.getR());
	}

}

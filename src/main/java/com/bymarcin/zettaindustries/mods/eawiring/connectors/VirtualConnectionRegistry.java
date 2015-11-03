package com.bymarcin.zettaindustries.mods.eawiring.connectors;

import java.util.LinkedList;
import java.util.Queue;

import com.bymarcin.zettaindustries.mods.eawiring.connection.AbstractConnection;

import mods.eln.Eln;
import mods.eln.sim.IProcess;

public class VirtualConnectionRegistry implements IProcess {
	private static final VirtualConnectionRegistry instance = new VirtualConnectionRegistry();
	private Queue<AbstractConnection> connections = new LinkedList<AbstractConnection>();

	private VirtualConnectionRegistry() {
		Eln.simulator.addSlowProcess(this);
	}

	public static VirtualConnectionRegistry instance() {
		return instance;
	}

	@Override
	public void process(double arg0) {
		for (int i = 0; i < 5; i++) {
			AbstractConnection ac = connections.poll();
			if (ac != null) {
				ac.init();
			} else {
				return;
			}
		}
	}

	public void addToQueue(AbstractConnection ac) {
		if (ac != null)
			connections.add(ac);
	}

}

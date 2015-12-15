package com.bymarcin.zettaindustries.mods.eawiring;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.block.EAConnector;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.block.EAConnectorRender;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.items.ItemEAConnector;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.items.ItemEAWireCoil;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.node.ConnectorNode;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorConventer;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorHV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorLV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorMV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorRHV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAWireConnector;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.wires.WireBase;
import com.bymarcin.zettaindustries.mods.eawiring.mosfet.Mosfet;
import com.bymarcin.zettaindustries.mods.eawiring.mosfet.MosfetNode;
import com.bymarcin.zettaindustries.mods.eawiring.mosfet.MosfetRender;
import com.bymarcin.zettaindustries.mods.eawiring.mosfet.TEMosfet;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import mods.eln.node.NodeManager;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class EAWiring implements IMod, IProxy{

	public static EAConnector connectorBlock = new EAConnector();
	public static ItemEAWireCoil wires = new ItemEAWireCoil();
	public static Mosfet mosfet;

	@Override
	public void preInit() {
		ZIRegistry.registerProxy(this);
		mosfet = new Mosfet();
		GameRegistry.registerTileEntity(TileEntityEAConnectorLV.class, "TileEntityEAConnectorLV");
		GameRegistry.registerTileEntity(TileEntityEAConnectorMV.class, "TileEntityEAConnectorMV");
		GameRegistry.registerTileEntity(TileEntityEAConnectorHV.class, "TileEntityEAConnectorHV");
		GameRegistry.registerTileEntity(TileEntityEAConnectorRHV.class, "TileEntityEAConnectorRHV");
		GameRegistry.registerTileEntity(TileEntityEAWireConnector.class, "TileEntityEAWireConnector");
		GameRegistry.registerTileEntity(TileEntityEAConnectorConventer.class, "TileEntityEAConnectorConventer");
		
		GameRegistry.registerTileEntity(TEMosfet.class, "TEMosfet");

		GameRegistry.registerItem(wires, "EAWireCoil");
		GameRegistry.registerBlock(connectorBlock, ItemEAConnector.class, "EAConnector");
		WireBase.getValues().add(WireBase.LVWire);
		WireBase.getValues().add(WireBase.MVWire);
		WireBase.getValues().add(WireBase.HVWire);
		
	}

	@Override
	public void init() {
		registerEnergyConverter();

	}

	@Override
	public void postInit() {
		
	}

	
	public void registerEnergyConverter() {
			NodeManager.registerUuid(ConnectorNode.getNodeID(), ConnectorNode.class);
			NodeManager.registerUuid(MosfetNode.getNodeUuidStatic(), MosfetNode.class);
	}

	@Override
	public void clientSide() {
		RenderingRegistry.registerBlockHandler(new MosfetRender());
		RenderingRegistry.registerBlockHandler(new EAConnectorRender());
	}

	@Override
	public void serverSide() {
	}	
}

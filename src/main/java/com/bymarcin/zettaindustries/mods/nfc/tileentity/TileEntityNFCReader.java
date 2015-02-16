package com.bymarcin.zettaindustries.mods.nfc.tileentity;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

public class TileEntityNFCReader extends TileEntityEnvironment{

    public TileEntityNFCReader() {
    	 node = Network.newNode(this, Visibility.Network).create();
    }
    
    public void sendEvent(String sennder, String data){
    	System.out.println("EVENT");
    	if(node!=null){
    		System.out.println("NOT NULL");
    		node.sendToReachable("computer.signal","nfc_data",sennder, data);
    		System.out.println(node.network());
    		System.out.println("isConn:" + this.addedToNetwork);
    	}
    }
    
    @Override
    public void update() {
    	super.update();
    	System.out.println("AAAA");
    }
}

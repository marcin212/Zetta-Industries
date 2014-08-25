package com.bymarcin.zettaindustries.mods.nfc.tileentity;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import cpw.mods.fml.common.Optional;
import net.minecraft.tileentity.TileEntity;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityNFCProgrammer extends TileEntity implements SimpleComponent{
	public String NFCData = null;
	
	@Override
	public String getComponentName() {
		return "NFCProgrammer";
	}
	
	@Callback
	public Object[] writeNFCData(Context contex, Arguments args){
		if(args.count()==1 && args.checkString(0).length()<=1024){
			NFCData = args.checkString(0);
			worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 2);
		}else{
			new Exception("No arguments or data is bigger than 1024 characters.");
		}
		return null;
	}
	
	@Callback
	public Object[] clearNFCData(Context contex, Arguments args){
		NFCData = null;
		worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 2);
		return null;
	}
	
	@Callback
	public Object[] isDataWaiting(Context contex, Arguments args){
		return new Object[]{(NFCData!=null)};
	}
	
	public String writeCardNFC(){
		String temp = NFCData;
		NFCData = null;
		return temp;
	}
	
}

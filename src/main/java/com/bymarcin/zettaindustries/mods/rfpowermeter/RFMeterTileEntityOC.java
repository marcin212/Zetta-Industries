package com.bymarcin.zettaindustries.mods.rfpowermeter;

import cpw.mods.fml.common.Optional;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class RFMeterTileEntityOC extends RFMeterTileEntity implements SimpleComponent{

	
	@Override
	public String getComponentName() {
		return "rfmeter";
	}
	
	public boolean checkPassword(int pos, final Arguments args){
		if(args.isString(pos)){
			if(canEdit(args.checkString(pos)))
				return true;
		}else{
			if(canEdit(null)){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * OpenComputers methods
	 */
	
	@Callback(doc = "function(password:string [, oldPassword:string]):bool")
	public Object[] setPassword(final Context context, final Arguments args) {
		if(!checkPassword(1,args)) return new Object[]{false};
		
		String password = args.checkString(0);
		setPassword(password);
		return new Object[]{true};
	}
	
	@Callback(doc = "function(password:string):bool")
	public Object[] removePassword(final Context context, final Arguments args){
		if(!checkPassword(0,args)) return new Object[]{false};
		removePassword();
		return new Object[]{true};
	}
	
	@Callback(doc = "function(name:string [, password:string]):bool")
	public Object[] setName(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		name = args.checkString(0)!=null?args.checkString(0):"";
		return new Object[]{true};
	}
	
	@Callback(doc = "function():string")
	public Object[] getName(final Context context, final Arguments args){
		return new Object[]{name};
	}
	
	public Object[] getAvg(Context ctx, Arguments arg){
		return new Object[]{transfer};
	}
	
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOn(final Context context, final Arguments args){
		if(!checkPassword(0,args)) return new Object[]{false};
		isOn = true;
		return new Object[]{true};
	}
	
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOff(final Context context, final Arguments args){
		if(!checkPassword(0,args)) return new Object[]{false};
		isOn = false;
		return new Object[]{true};
	}

	@Callback(doc = "function(value:int [, password:string]):bool")
	public Object[] setEnergyCounter(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		value = lastValue = args.checkInteger(0);
		return new Object[]{true};
	}
	
	@Callback(doc = "function():string")
	public Object[] getCounterMode(final Context context, final Arguments args){
		String type = inCounterMode?"counter":"prepaid";
		return new Object[]{type};
	}
	
	@Callback(doc = "function(type:bool [, password:string]):bool -- true == counter, false == prepaid")
	public Object[] setCounterMode(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		inCounterMode = args.checkBoolean(0);
		return new Object[]{true};
	}
	
	@Callback(doc = "function():double")
	public Object[] getCounterValue(final Context context, final Arguments args){
		return new Object[]{value};
	}
	
	@Callback(doc = "function():bool")
	public Object[] canEnergyFlow(final Context context, final Arguments args){
		return new Object[]{canEnergyFlow()};
	}
	
	@Callback(doc = "function(limit:int [, password:string]):bool")
	public Object[] setLimitPerTick(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		if(args.checkInteger(0)>=0)
			transferLimit = args.checkInteger(0);
		else
			transferLimit = -1;
		return new Object[]{true};
	}
	
	@Callback(doc = "function([password:string]):bool")
	public Object[] changeFlowDirection(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		invert();
		return new Object[]{true};
	}
	
	/*
	 * end 
	 * 
	 */
	
	
}

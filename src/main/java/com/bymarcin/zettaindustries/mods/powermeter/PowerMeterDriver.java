package com.bymarcin.zettaindustries.mods.powermeter;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.occ.mods.ManagedTileEntityEnvironment;
import net.minecraft.world.World;
import buildcraft.api.transport.IPipeTile;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;

public class PowerMeterDriver extends DriverTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return IPipeTile.class;
	}

	@Override
	public ManagedEnvironment createEnvironment(final World world, final int x, final int y, final int z) {
		TileGenericPipe  pipe = (TileGenericPipe ) world.getTileEntity(x, y, z);
		if(pipe!=null && pipe.pipe instanceof PowerMeterPipe)
			return new Environment(pipe,pipe.pipe);
		else
			return null;
	}

	public static final class Environment extends ManagedTileEntityEnvironment<IPipeTile> {
		PowerMeterPipe pipe;
		@SuppressWarnings("rawtypes")
		public Environment(final IPipeTile tileEntity, Pipe pipe) {
			super(tileEntity, "power_meter");
			this.pipe = (PowerMeterPipe) pipe;
		}
		
		public boolean checkPassword(int pos, final Arguments args){
			if(args.isString(pos)){
				if(pipe.canEdit(args.checkString(pos)))
					return true;
			}else{
				if(pipe.canEdit(null)){
					return true;
				}
			}
			return false;
		}

		@Callback(doc = "function(password:string [, oldPassword:string]):bool")
		public Object[] setPassword(final Context context, final Arguments args) {
			if(!checkPassword(1,args)) return new Object[]{false};
			
			String password = args.checkString(0);
			pipe.setPassword(password);
			return new Object[]{true};
		}
		
		@Callback(doc = "function():double")
		public Object[] getAvg(final Context context, final Arguments args){
			return new Object[]{pipe.getAvg()};
		}
		
		@Callback(doc = "function(name:string [, password:string]):bool")
		public Object[] setName(final Context context, final Arguments args){
			if(!checkPassword(1,args)) return new Object[]{false};
			pipe.setName(args.checkString(0)!=null?args.checkString(0):"");
			return new Object[]{true};
		}
		
		@Callback(doc = "function():string")
		public Object[] getName(final Context context, final Arguments args){
			return new Object[]{pipe.getName()};
		}
		
		@Callback(doc = "function([password:string]):bool")
		public Object[] setOn(final Context context, final Arguments args){
			if(!checkPassword(0,args)) return new Object[]{false};
			pipe.setState(true);
			return new Object[]{true};
		}
		
		@Callback(doc = "function([password:string]):bool")
		public Object[] setOff(final Context context, final Arguments args){
			if(!checkPassword(0,args)) return new Object[]{false};
			pipe.setState(false);
			return new Object[]{true};
		}
		
		@Callback(doc = "function(password:string):bool")
		public Object[] removePassword(final Context context, final Arguments args){
			if(!checkPassword(0,args)) return new Object[]{false};
			pipe.removePassword();
			return new Object[]{true};
		}
		
		@Callback(doc = "function(value:int [, password:string]):bool")
		public Object[] setEnergyCounter(final Context context, final Arguments args){
			if(!checkPassword(1,args)) return new Object[]{false};
			pipe.setEnergyCounter(args.checkInteger(0));
			return new Object[]{true};
		}
		
		@Callback(doc = "function():string")
		public Object[] getCounterMode(final Context context, final Arguments args){
			String type = pipe.getCounterMode()?"prepaid":"counter";
			return new Object[]{type};
		}
		
		@Callback(doc = "function():double")
		public Object[] getCounterValue(final Context context, final Arguments args){
			return new Object[]{pipe.getEnergyUsed()};
		}
		
		@Callback(doc = "function(type:bool [, password:string]):bool -- true == counter, false == prepaid")
		public Object[] setCounterMode(final Context context, final Arguments args){
			if(!checkPassword(1,args)) return new Object[]{false};
			pipe.setCounterMode(args.checkBoolean(0));
			return new Object[]{true};
		}
		
		@Callback(doc = "function():bool")
		public Object[] canFlowEnergy(final Context context, final Arguments args){
			return new Object[]{pipe.canFlowEnergy()};
		}

	}
}

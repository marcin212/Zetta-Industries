package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.utils.WorldUtils;
import com.bymarcin.zettaindustries.utils.render.RenderUtils;
import com.cout970.magneticraft.api.electricity.ElectricConstants;
import com.cout970.magneticraft.api.electricity.ElectricUtils;
import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.electricity.IEnergyInterface;
import com.cout970.magneticraft.api.electricity.IIndexedConnection;
import com.cout970.magneticraft.api.electricity.prefab.ElectricConductor;
import com.cout970.magneticraft.api.electricity.prefab.IndexedConnection;
import com.cout970.magneticraft.api.util.ConnectionClass;
import com.cout970.magneticraft.api.util.MgDirection;
import com.cout970.magneticraft.api.util.MgUtils;
import com.cout970.magneticraft.api.util.VecInt;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;

import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyHandler;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

@Optional.InterfaceList({
		@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers"),
		@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = "ComputerCraft")
})
public class RFMeterTileEntityOC extends RFMeterTileEntity implements SimpleComponent, IPeripheralProvider, IElectricTile {

	
	
	
	@Override
	public String getComponentName() {
		return "rfmeter";
	}

	public boolean checkPassword(int pos, final Arguments args) {
		if (args.isString(pos)) {
			if (canEdit(args.checkString(pos)))
				return true;
		} else {
			if (canEdit(null)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * OpenComputers methods
	 */
	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function(password:string [, oldPassword:string]):bool")
	public Object[] setPassword(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };

		String password = args.checkString(0);
		setPassword(password);
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function(password:string):bool")
	public Object[] removePassword(final Context context, final Arguments args) {
		if (!checkPassword(0, args))
			return new Object[] { false };
		removePassword();
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function(name:string [, password:string]):bool")
	public Object[] setName(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		name = args.checkString(0) != null ? args.checkString(0) : "";
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function():string")
	public Object[] getName(final Context context, final Arguments args) {
		return new Object[] { name };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function():number")
	public Object[] getAvg(Context ctx, Arguments arg) {
		return new Object[] { transfer };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOn(final Context context, final Arguments args) {
		if (!checkPassword(0, args))
			return new Object[] { false };
		isOn = true;
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOff(final Context context, final Arguments args) {
		if (!checkPassword(0, args))
			return new Object[] { false };
		isOn = false;
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function(value:int [, password:string]):bool")
	public Object[] setEnergyCounter(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		value = lastValue = args.checkInteger(0);
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function():string")
	public Object[] getCounterMode(final Context context, final Arguments args) {
		String type = inCounterMode ? "counter" : "prepaid";
		return new Object[] { type };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function(type:bool [, password:string]):bool -- true == counter, false == prepaid")
	public Object[] setCounterMode(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		inCounterMode = args.checkBoolean(0);
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function():double")
	public Object[] getCounterValue(final Context context, final Arguments args) {
		return new Object[] { value };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function():bool")
	public Object[] canEnergyFlow(final Context context, final Arguments args) {
		return new Object[] { canEnergyFlow() };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function(limit:int [, password:string]):bool")
	public Object[] setLimitPerTick(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		if (args.checkInteger(0) >= 0)
			transferLimit = args.checkInteger(0);
		else
			transferLimit = -1;
		return new Object[] { true };
	}

	@Optional.Method(modid = "OpenComputers")
	@Callback(doc = "function([password:string]):bool")
	public Object[] changeFlowDirection(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		invert();
		return new Object[] { true };
	}

	// ComputerCraft

	@Optional.Method(modid = "ComputerCraft")
	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof RFMeterTileEntityOC)
			return RenderUtils.FORGE_DIRECTIONS[world.getBlockMetadata(x, y, z)].ordinal() == side ? new RFMeterCCPeripheral((RFMeterTileEntityOC) te) : null;
		else
			return null;
	}

	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft")
	public static class RFMeterCCPeripheral implements IPeripheral {
		RFMeterTileEntityOC te;

		public RFMeterCCPeripheral(RFMeterTileEntityOC te) {
			this.te = te;
		}

		@Optional.Method(modid = "ComputerCraft")
		@Override
		public String getType() {
			return "rfmeter";
		}

		@Optional.Method(modid = "ComputerCraft")
		@Override
		public String[] getMethodNames() {
			return new String[] {
					"getAVG",
					"canEnergyFlow",
					"changeFlowDirection",
					"getCounterMode",
					"getCounterValue",
					"getName",
					"removePassword",
					"setCounterMode",
					"setEnergyCounter",
					"setLimitPerTick",
					"setName",
					"setOff",
					"setOn",
					"setPassword"
			};
		}

		@Optional.Method(modid = "ComputerCraft")
		@Override
		public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
			switch (method) {
			case 0:// getAvg
				return new Object[] { te.transfer };
			case 1:// canEnergyFlow
				return new Object[] { te.canEnergyFlow() };
			case 2:// changeFlowDirection
				if (!checkPassword(1, arguments))
					return new Object[] { false };
				te.invert();
				return new Object[] { true };
			case 3:// getCounterMode
				String type = te.inCounterMode ? "counter" : "prepaid";
				return new Object[] { type };
			case 4:// getCounterValue
				return new Object[] { te.value };
			case 5:// getName
				return new Object[] { te.name };
			case 6:// removePassword
				if (!checkPassword(0, arguments))
					return new Object[] { false };
				te.removePassword();
				return new Object[] { true };
			case 7:// setCounterMode
				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof Boolean))
					return new Object[] { false };
				te.inCounterMode = (Boolean) arguments[0];
				return new Object[] { true };
			case 8:// setEnergyCounter
				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof Double))
					return new Object[] { false };
				te.value = te.lastValue = ((Double) arguments[0]).intValue();
				return new Object[] { true };
			case 9:// setLimitPerTick
				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof Double))
					return new Object[] { false };
				if (((Double) arguments[0]).intValue() >= 0)
					te.transferLimit = ((Double) arguments[0]).intValue();
				else
					te.transferLimit = -1;
				return new Object[] { true };
			case 10:// setName
				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof String))
					return new Object[] { false };
				te.name = (String) arguments[0];
				return new Object[] { true };
			case 11:// setOff
				if (!checkPassword(0, arguments))
					return new Object[] { false };
				te.isOn = false;
				return new Object[] { true };
			case 12:// setOn
				if (!checkPassword(0, arguments))
					return new Object[] { false };
				te.isOn = true;
				return new Object[] { true };
			case 13:// setPassword
				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof String))
					return new Object[] { false };
				String password = (String) arguments[0];
				te.setPassword(password);
				return new Object[] { true };

			}
			return null;
		}

		@Optional.Method(modid = "ComputerCraft")
		public boolean checkPassword(int pos, Object[] args) {
			if (args != null && pos < args.length) {
				if (args[pos] instanceof String)
					return te.canEdit((String) args[pos]);
			} else {
				if (te.canEdit(null)) {
					return true;
				}
			}
			return false;
		}

		@Optional.Method(modid = "ComputerCraft")
		@Override
		public void attach(IComputerAccess computer) {

		}

		@Optional.Method(modid = "ComputerCraft")
		@Override
		public void detach(IComputerAccess computer) {

		}

		@Optional.Method(modid = "ComputerCraft")
		@Override
		public boolean equals(IPeripheral other) {
			if (other == null) {
				return false;
			}
			if (this == other) {
				return true;
			}
			if (other instanceof TileEntity) {
				TileEntity tother = (TileEntity) other;
				return tother.getWorldObj().equals(te.worldObj)
						&& tother.xCoord == te.xCoord && tother.yCoord == te.yCoord && tother.zCoord == te.zCoord;
			}

			return false;
		}

	}


	/*
	 * end
	 */
	
	
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.isRemote) return;
		cond.recache();
		cond.iterate();
		
		if(!canEnergyFlow()){
			highImpedance();
		}else{
			 lowImpedance();
		}
		 
		 if(!inCounterMode && value<=0){
			 highImpedance();
		 }else{
			 lowImpedance();
		 }

		
	}

	public void writeToNBT(net.minecraft.nbt.NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		cond.save(nbt);
	}
	
	public void readFromNBT(net.minecraft.nbt.NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		cond.load(nbt);
	}
	
	IElectricConductor cond = initConductor();

	/*conductor*/
	
	@Override
	public IElectricConductor[] getConds(VecInt dir, int tier) {
		return tier==1 &&(dir.getY()==-1 || dir.getY()==1 || VecInt.NULL_VECTOR.equals(dir))?new IElectricConductor[]{cond}:null;
	}

	    public IElectricConductor initConductor() {
	        return new ElectricConductor(this,1,ElectricConstants.RESISTANCE_COPPER_MED) {
	        	
	        	@Override
	            public void recache() {
	                if (!connected) {
	                    connected = true;
	                    con.clear();
	                    int sides = 0;
	                    for (VecInt f : getValidConnections()) {//search for other conductors

	                        TileEntity target = MgUtils.getTileEntity(tile, f);
	                        IElectricConductor[] c = ElectricUtils.getElectricCond(target, f.getOpposite(), getTier());
	                        IEnergyInterface inter = ElectricUtils.getInterface(target, f.getOpposite(), getTier());

	                        if (c != null) {
	                            for (IElectricConductor e : c) {
	                                if (e == this) continue;
	                                if (this.isAbleToConnect(e, f) && e.isAbleToConnect(this, f.getOpposite())) {
	                                	con.add(new IndexedConnection(this, f, e, sides));
	                                	sides++;
	                                }
	                            }
	                        }
	                        if (inter != null && inter.canConnect(f)) {
	                            con.add(new IndexedConnection(this, f, inter, sides));
	                            sides++;
	                        }
	                    }
	                    if (currents == null) {
	                        currents = new double[sides];
	                    } else {
	                        if (currents.length != sides) {
	                            double[] temp = new double[sides];
	                            System.arraycopy(currents, 0, temp, 0, Math.min(sides, currents.length));
	                            currents = temp;
	                        }
	                    }
	                }
	            }
	        	
	        	@Override
	            public void iterate() {
	                TileEntity tile = getParent();
	                World w = tile.getWorldObj();
	                //only calculated on server side
	                if (w.isRemote) return;
	                tile.markDirty();
	                //make sure the method computeVoltage was called
	                this.getVoltage();
	                for (IIndexedConnection f : con) {
	                	valance_diode(f, currents);
	                }
	            }

	        	public void valance_diode(IIndexedConnection f, double[] currents) {
	                IElectricConductor cond = f.getConductor();
	                IEnergyInterface c = f.getEnergyInterface();
	                if (cond != null) {
	                    //the resistance of the connection
	                    double resistence = (f.getSource().getResistance() + cond.getResistance());
	                    //the voltage differennce
	                    double deltaV = f.getSource().getVoltage() - cond.getVoltage();
	                    //sanity check for infinite current
	                    if (Double.isNaN(currents[f.getIndex()])) currents[f.getIndex()] = 0;
	                     
	                    if((f.getOffset().equals(getDirection().toVecInt()) && deltaV > 0) || !f.getOffset().equals(getDirection().toVecInt())){
	                    	//the extra current from the last tick
	                    	double current = currents[f.getIndex()];
	                    	// (V - I*R) I*R is the voltage difference that this conductor should have using the ohm's law, and V the real one
	                    	//vDiff is the voltage difference bvetween the current voltager difference and the proper voltage difference using the ohm's law
	                    	double vDiff = (deltaV - current * resistence);
	                    	//make sure the vDiff is not in the incorrect direction when the resistance is too big
	                    	vDiff = Math.min(vDiff, Math.abs(deltaV));
	                    	vDiff = Math.max(vDiff, -Math.abs(deltaV));
	                    	// add to the next tick current an extra to get the proper voltage difference on the two conductors
	                    	currents[f.getIndex()] += (vDiff * f.getSource().getIndScale()) / f.getSource().getVoltageMultiplier();
	                    	// to the extra current add the current generated by the voltage difference
	                    	current += (deltaV * 0.5D) / (f.getSource().getVoltageMultiplier());
	                    	//moves the charge
	                    	f.getSource().applyCurrent(-current);
	                    	if(f.getOffset().equals(getDirection().toVecInt())){
	                   		 if(inCounterMode){
	                   			value+= current*cond.getVoltage()/cond.getVoltageMultiplier();
	                 		 }else{
	                 			value-= current*cond.getVoltage()/cond.getVoltageMultiplier();
	                 		 }
	                    	}
	                    	cond.applyCurrent(current);
	                    }
	                }
	                if (c != null) {
	                    if (f.getSource().getVoltage() > ElectricConstants.ENERGY_INTERFACE_LEVEL && c.canAcceptEnergy(f)) {
	                        double watt = Math.min(c.getMaxFlow(), (f.getSource().getVoltage() - ElectricConstants.ENERGY_INTERFACE_LEVEL) * ElectricConstants.CONVERSION_SPEED);
	                        if (watt > 0){
	                            f.getSource().drainPower(c.applyEnergy(watt));
	                           
	                        }
	                    }
	                }
	            }
	        	
	            @Override
	            public VecInt[] getValidConnections() {
	                return new VecInt[]{getDirection().toVecInt(), getDirection().opposite().toVecInt()};
	            }

	            @Override
	            public ConnectionClass getConnectionClass(VecInt v) {
	                return ConnectionClass.Cable_MEDIUM;
	            }
	            
	            @Override
	            public boolean canFlowPower(IIndexedConnection con) {
	                return false;
	            }
	        };
	    }

	  public void highImpedance(){
		  cond.setResistance(1E19);
	  }
	  
	  public void lowImpedance(){
		  cond.setResistance(ElectricConstants.RESISTANCE_COPPER_LOW);
	  }
	    
	  public MgDirection getDirection(){
		  return isInverted?MgDirection.UP:MgDirection.DOWN;
	  }
	  
	  @Override
	public void invert() {
		super.invert();
		cond.disconnect();
	}
	  
	  
}

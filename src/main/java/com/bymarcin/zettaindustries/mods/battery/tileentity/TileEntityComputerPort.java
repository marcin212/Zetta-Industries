package com.bymarcin.zettaindustries.mods.battery.tileentity;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;

import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityComputerPort extends BasicRectangularMultiblockTileEntityBase implements SimpleComponent{

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {		
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void onMachineActivated() {

	}

	@Override
	public void onMachineDeactivated() {

	}
	
	@Override
	public String getComponentName() {
		return "big_battery";
	}
	
	private BatteryController getControler(){
		return (BatteryController) getMultiblockController();
	}
	
	private TileEntityPowerTap findPowerTap(int id){
		if(getControler()!=null){
			TileEntityPowerTap[] array = getControler().getPowerTaps().toArray(new TileEntityPowerTap[]{});
			if(id >=0 && id < array.length){
				return array[id];
			}
		}
		return null;
	}
	
	private TileEntityPowerTap findPowerTap(String label){
		if(getControler()!=null){
			TileEntityPowerTap[] array = getControler().getPowerTaps().toArray(new TileEntityPowerTap[]{});
			for(int i=0; i<array.length; i++){
				if(array[i].getLabel().equals(label)){
					return array[i];
				}
			}
		}
		return null;
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] setIn(Context c, Arguments args){
		String name = null;
		int id = -1;
		if(args.isString(0)){
			name = args.checkString(0);
		}else{
			id = args.checkInteger(0);
		}
		
		TileEntityPowerTap powerTap = name!=null?findPowerTap(name):findPowerTap(id);
		if(powerTap!=null){
					powerTap.setIn();
				return null;
		}else{
				return new Object[]{null,"Electrode or Controller not found."};
		}
	}
	
	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] setOut(Context c, Arguments args){
		String name = null;
		int id = -1;
		if(args.isString(0)){
			name = args.checkString(0);
		}else{
			id = args.checkInteger(0);
		}
		
		TileEntityPowerTap powerTap = name!=null?findPowerTap(name):findPowerTap(id);
		if(powerTap!=null){
					powerTap.setOut();
				return null;
		}else{
				return new Object[]{null,"Electrode or Controller not found."};
		}
	}
	
    @Callback
    @Optional.Method(modid = "OpenComputers")
	public Object[] getEnergyStored(Context c, Arguments args){
		if(getControler()!=null)
			return new Object[]{getControler().getStorage().getRealEnergyStored()};
		return new Object[]{null,"Controller block not found. Rebuild your battery."};
	}
	
    @Callback
    @Optional.Method(modid = "OpenComputers")
	public Object[] getMaxEnergyStored(Context c, Arguments args){
		if(getControler()!=null)
			return new Object[]{getControler().getStorage().getRealMaxEnergyStored()};
		return new Object[]{null,"Controler block not found. Rebuild your battery."};
	}
	
    @Callback
    @Optional.Method(modid = "OpenComputers")
	public Object[] setElectrodeTransfer(Context c, Arguments args){
    	String label = null;
    	int id = -1;
    	if(args.isString(0)){
    		label = args.checkString(0);
    	}else{
    		id = args.checkInteger(0);
    	}
    	
		int transfer = args.checkInteger(1);
		TileEntityPowerTap powerTap = label!=null?findPowerTap(label):findPowerTap(id);
		if(powerTap!=null){	
				powerTap.setTransfer(transfer);
				return null;
		}else{
				return new Object[]{null,"Electrode or Controller not found."};
		}
	}
	
    @Callback
    @Optional.Method(modid = "OpenComputers")
	public Object[] setAllElectrodeTransfer(Context c, Arguments args){
		int transfer = args.checkInteger(0);
		if(getControler()!=null){	
				for(TileEntityPowerTap tap: getControler().getPowerTaps())
						tap.setTransfer(transfer);
				return null;
		}
		return new Object[]{null,"Controller block not found. Rebuild your battery."};
	}
	
    @Callback
    @Optional.Method(modid = "OpenComputers")
	public Object[] getMaxElectrodeTransfer(Context c, Arguments args){
		if(getControler()!=null)
			return new Object[]{getControler().getStorage().getMaxExtract()};
		return new Object[]{null,"Controller block not found. Rebuild your battery."};
	}
	
    @Callback
    @Optional.Method(modid = "OpenComputers")
	public Object[] getEnergyBalanceLastTick(Context c, Arguments args){
		if(getControler()!=null)
			return new Object[]{getControler().getLastTickBalance()};
		return new Object[]{null,"Controller block not found. Rebuild your battery."};
	}
}

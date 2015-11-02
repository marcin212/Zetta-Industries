package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.utils.render.RenderUtils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

@Optional.InterfaceList({
		@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers"),
		@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft"),
		@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = "ComputerCraft")
})
public class RFMeterTileEntityOC extends RFMeterTileEntity implements SimpleComponent, IPeripheral, IPeripheralProvider {

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
			return new Object[]{ transfer };
		case 1:// canEnergyFlow
			return new Object[] { canEnergyFlow() };
		case 2:// changeFlowDirection
			if (!checkPassword(1, arguments))
				return new Object[] { false };
			invert();
			return new Object[] { true };
		case 3:// getCounterMode
			String type = inCounterMode ? "counter" : "prepaid";
			return new Object[] { type };
		case 4:// getCounterValue
			return new Object[] { value };
		case 5:// getName
			return new Object[]{ name };
		case 6:// removePassword
			if (!checkPassword(0, arguments))
				return new Object[] { false };
			removePassword();
			return new Object[] { true };
		case 7:// setCounterMode
			if (!checkPassword(1, arguments) || !(arguments.length>0 && arguments[0] instanceof Boolean))
				return new Object[] { false };
			inCounterMode = (Boolean)arguments[0];
			return new Object[] { true };
		case 8:// setEnergyCounter
			if (!checkPassword(1, arguments) || !(arguments.length>0 && arguments[0] instanceof Double))
				return new Object[] { false };
			value = lastValue = ((Double)arguments[0]).intValue();
			return new Object[] { true };
		case 9:// setLimitPerTick
			if (!checkPassword(1, arguments) || !(arguments.length>0 && arguments[0] instanceof Double))
				return new Object[] { false };
			if ( ((Double)arguments[0]).intValue() >= 0)
				transferLimit = ((Double)arguments[0]).intValue();
			else
				transferLimit = -1;
			return new Object[] { true };
		case 10:// setName
			if (!checkPassword(1, arguments) || !(arguments.length>0 && arguments[0] instanceof String))
				return new Object[] { false };
			name = (String)arguments[0];
			return new Object[] { true };
		case 11:// setOff
			if (!checkPassword(0, arguments))
				return new Object[] { false };
			isOn = false;
			return new Object[] { true };
		case 12:// setOn
			if (!checkPassword(0, arguments))
				return new Object[] { false };
			isOn = true;
			return new Object[] { true };
		case 13:// setPassword
			if (!checkPassword(1, arguments) || !(arguments.length>0 && arguments[0] instanceof String))
				return new Object[] { false };
			String password = (String)arguments[0];
			setPassword(password);
			return new Object[] { true };

		}
		return null;
	}
	
	@Optional.Method(modid = "ComputerCraft")
	public boolean checkPassword(int pos, Object[] args) {
		if (args!=null && pos<args.length) {
			if (args[pos] instanceof String)
				return canEdit((String)args[pos]);
		} else {
			if (canEdit(null)) {
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
		if(other == null) {
			return false;
		}
		if(this == other) {
			return true;
		}
		if(other instanceof TileEntity) {
			TileEntity tother = (TileEntity) other;
			return tother.getWorldObj().equals(worldObj)
				&& tother.xCoord == this.xCoord && tother.yCoord == this.yCoord && tother.zCoord == this.zCoord;
		}

		return false;
	}
	
	@Optional.Method(modid = "ComputerCraft")
	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		return RenderUtils.FORGE_DIRECTIONS[world.getBlockMetadata(x, y, z)].ordinal() == side?this:null;
	}

	/*
	 * end
	 */

}

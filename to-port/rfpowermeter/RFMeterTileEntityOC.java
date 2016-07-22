package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.utils.render.RenderUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


//import dan200.computercraft.api.lua.ILuaContext;
//import dan200.computercraft.api.lua.LuaException;
//import dan200.computercraft.api.peripheral.IComputerAccess;
//import dan200.computercraft.api.peripheral.IPeripheral;
//import dan200.computercraft.api.peripheral.IPeripheralProvider;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

import net.minecraftforge.fml.common.Optional;


@Optional.InterfaceList({
		@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
		//,@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = "ComputerCraft")
})
public class RFMeterTileEntityOC extends RFMeterTileEntity implements SimpleComponent 
//,IPeripheralProvider 
{

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		super.readFromNBT(nbt);
	}
	
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

//	@Optional.Method(modid = "ComputerCraft")
//	@Override
//	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
//		TileEntity te = world.getTileEntity(x, y, z);
//		if (te instanceof RFMeterTileEntityOC)
//			return RenderUtils.FORGE_DIRECTIONS[world.getBlockMetadata(x, y, z)].ordinal() == side ? new RFMeterCCPeripheral((RFMeterTileEntityOC) te) : null;
//		else
//			return null;
//	}

//	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft")
//	public static class RFMeterCCPeripheral implements IPeripheral {
//		RFMeterTileEntityOC te;
//
//		public RFMeterCCPeripheral(RFMeterTileEntityOC te) {
//			this.te = te;
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public String getType() {
//			return "rfmeter";
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public String[] getMethodNames() {
//			return new String[] {
//					"getAVG",
//					"canEnergyFlow",
//					"changeFlowDirection",
//					"getCounterMode",
//					"getCounterValue",
//					"getName",
//					"removePassword",
//					"setCounterMode",
//					"setEnergyCounter",
//					"setLimitPerTick",
//					"setName",
//					"setOff",
//					"setOn",
//					"setPassword"
//			};
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
//			switch (method) {
//			case 0:// getAvg
//				return new Object[] { te.transfer };
//			case 1:// canEnergyFlow
//				return new Object[] { te.canEnergyFlow() };
//			case 2:// changeFlowDirection
//				if (!checkPassword(1, arguments))
//					return new Object[] { false };
//				te.invert();
//				return new Object[] { true };
//			case 3:// getCounterMode
//				String type = te.inCounterMode ? "counter" : "prepaid";
//				return new Object[] { type };
//			case 4:// getCounterValue
//				return new Object[] { te.value };
//			case 5:// getName
//				return new Object[] { te.name };
//			case 6:// removePassword
//				if (!checkPassword(0, arguments))
//					return new Object[] { false };
//				te.removePassword();
//				return new Object[] { true };
//			case 7:// setCounterMode
//				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof Boolean))
//					return new Object[] { false };
//				te.inCounterMode = (Boolean) arguments[0];
//				return new Object[] { true };
//			case 8:// setEnergyCounter
//				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof Double))
//					return new Object[] { false };
//				te.value = te.lastValue = ((Double) arguments[0]).intValue();
//				return new Object[] { true };
//			case 9:// setLimitPerTick
//				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof Double))
//					return new Object[] { false };
//				if (((Double) arguments[0]).intValue() >= 0)
//					te.transferLimit = ((Double) arguments[0]).intValue();
//				else
//					te.transferLimit = -1;
//				return new Object[] { true };
//			case 10:// setName
//				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof String))
//					return new Object[] { false };
//				te.name = (String) arguments[0];
//				return new Object[] { true };
//			case 11:// setOff
//				if (!checkPassword(0, arguments))
//					return new Object[] { false };
//				te.isOn = false;
//				return new Object[] { true };
//			case 12:// setOn
//				if (!checkPassword(0, arguments))
//					return new Object[] { false };
//				te.isOn = true;
//				return new Object[] { true };
//			case 13:// setPassword
//				if (!checkPassword(1, arguments) || !(arguments.length > 0 && arguments[0] instanceof String))
//					return new Object[] { false };
//				String password = (String) arguments[0];
//				te.setPassword(password);
//				return new Object[] { true };
//
//			}
//			return null;
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		public boolean checkPassword(int pos, Object[] args) {
//			if (args != null && pos < args.length) {
//				if (args[pos] instanceof String)
//					return te.canEdit((String) args[pos]);
//			} else {
//				if (te.canEdit(null)) {
//					return true;
//				}
//			}
//			return false;
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public void attach(IComputerAccess computer) {
//
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public void detach(IComputerAccess computer) {
//
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public boolean equals(IPeripheral other) {
//			if (other == null) {
//				return false;
//			}
//			if (this == other) {
//				return true;
//			}
//			if (other instanceof TileEntity) {
//				TileEntity tother = (TileEntity) other;
//				return tother.getWorldObj().equals(te.worldObj)
//						&& tother.xCoord == te.xCoord && tother.yCoord == te.yCoord && tother.zCoord == te.zCoord;
//			}
//
//			return false;
//		}
//
//	}

	/*
	 * end
	 */

}

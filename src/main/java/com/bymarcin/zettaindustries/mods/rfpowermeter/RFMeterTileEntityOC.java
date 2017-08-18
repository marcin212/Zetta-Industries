package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.utils.render.RenderUtils;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import li.cil.oc.api.*;
import li.cil.oc.api.network.*;
import li.cil.oc.api.network.Network;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


//import dan200.computercraft.api.lua.ILuaContext;
//import dan200.computercraft.api.lua.LuaException;
//import dan200.computercraft.api.peripheral.IComputerAccess;
//import dan200.computercraft.api.peripheral.IPeripheral;
//import dan200.computercraft.api.peripheral.IPeripheralProvider;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
		@Optional.Interface(iface = "li.cil.oc.api.network.Environment", modid = "opencomputers"),
		@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = "computercraft")
})
public class RFMeterTileEntityOC extends RFMeterTileEntity implements Environment, IPeripheralProvider {
	public RFMeterTileEntityOC(){
		node = li.cil.oc.api.Network.newNode(this, Visibility.Network).withComponent("rfmeter").create();
	}

//Enviroment
	protected Node node;
	protected boolean addedToNetwork = false;

	@Optional.Method(modid = "opencomputers")
	@Override
	public Node node() {
		return node;
	}

	@Optional.Method(modid = "opencomputers")
	@Override
	public void onConnect(final Node node) {

	}
	@Optional.Method(modid = "opencomputers")
	@Override
	public void onDisconnect(final Node node) {

	}
	@Optional.Method(modid = "opencomputers")
	@Override
	public void onMessage(final Message message) {

	}

	// ----------------------------------------------------------------------- //

	@Override
	public void update() {
		super.update();
		if (!addedToNetwork) {
			addedToNetwork = true;
			li.cil.oc.api.Network.joinOrCreateNetwork(this);
		}
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		// Make sure to remove the node from its network when its environment,
		// meaning this tile entity, gets unloaded.
		if (node != null) node.remove();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		// Make sure to remove the node from its network when its environment,
		// meaning this tile entity, gets unloaded.
		if (node != null) node.remove();
	}

	// ----------------------------------------------------------------------- //

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		// The host check may be superfluous for you. It's just there to allow
		// some special cases, where getNode() returns some node managed by
		// some other instance (for example when you have multiple internal
		// nodes in this tile entity).
		if (node != null && node.host() == this) {
			// This restores the node's address, which is required for networks
			// to continue working without interruption across loads. If the
			// node is a power connector this is also required to restore the
			// internal energy buffer of the node.
			node.load(nbt.getCompoundTag("oc:node"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		// See readFromNBT() regarding host check.
		if (node != null && node.host() == this) {
			final NBTTagCompound nodeNbt = new NBTTagCompound();
			node.save(nodeNbt);
			nbt.setTag("oc:node", nodeNbt);
		}
		return nbt;
	}




	//


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
	 * opencomputers methods
	 */
	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function(password:string [, oldPassword:string]):bool")
	public Object[] setPassword(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };

		String password = args.checkString(0);
		setPassword(password);
		return new Object[] { true };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function(password:string):bool")
	public Object[] removePassword(final Context context, final Arguments args) {
		if (!checkPassword(0, args))
			return new Object[] { false };
		removePassword();
		return new Object[] { true };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function(name:string [, password:string]):bool")
	public Object[] setName(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		name = args.checkString(0) != null ? args.checkString(0) : "";
		return new Object[] { true };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function():string")
	public Object[] getName(final Context context, final Arguments args) {
		return new Object[] { name };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function():number")
	public Object[] getAvg(Context ctx, Arguments arg) {
		return new Object[] { transfer };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOn(final Context context, final Arguments args) {
		if (!checkPassword(0, args))
			return new Object[] { false };
		isOn = true;
		return new Object[] { true };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOff(final Context context, final Arguments args) {
		if (!checkPassword(0, args))
			return new Object[] { false };
		isOn = false;
		return new Object[] { true };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function(value:int [, password:string]):bool")
	public Object[] setEnergyCounter(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		value = lastValue = args.checkInteger(0);
		return new Object[] { true };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function():string")
	public Object[] getCounterMode(final Context context, final Arguments args) {
		String type = inCounterMode ? "counter" : "prepaid";
		return new Object[] { type };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function(type:bool [, password:string]):bool -- true == counter, false == prepaid")
	public Object[] setCounterMode(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		inCounterMode = args.checkBoolean(0);
		return new Object[] { true };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function():double")
	public Object[] getCounterValue(final Context context, final Arguments args) {
		return new Object[] { value };
	}

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function():bool")
	public Object[] canEnergyFlow(final Context context, final Arguments args) {
		return new Object[] { canEnergyFlow() };
	}

	@Optional.Method(modid = "opencomputers")
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

	@Optional.Method(modid = "opencomputers")
	@Callback(doc = "function([password:string]):bool")
	public Object[] changeFlowDirection(final Context context, final Arguments args) {
		if (!checkPassword(1, args))
			return new Object[] { false };
		invert();
		return new Object[] { true };
	}

	// computercraft

	@Optional.Method(modid = "computercraft")
	@Override
	public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing side) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof RFMeterTileEntityOC)
			return side == world.getBlockState(pos).getValue(RFMeterBlock.front).getOpposite() ? new RFMeterCCPeripheral((RFMeterTileEntityOC) te) : null;
		else
			return null;
	}

	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "computercraft")
	public static class RFMeterCCPeripheral implements IPeripheral {
		RFMeterTileEntityOC te;

		public RFMeterCCPeripheral(RFMeterTileEntityOC te) {
			this.te = te;
		}

		@Optional.Method(modid = "computercraft")
		@Override
		public String getType() {
			return "rfmeter";
		}

		@Optional.Method(modid = "computercraft")
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

		@Optional.Method(modid = "computercraft")
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

		@Optional.Method(modid = "computercraft")
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

		@Optional.Method(modid = "computercraft")
		@Override
		public void attach(IComputerAccess computer) {

		}

		@Optional.Method(modid = "computercraft")
		@Override
		public void detach(IComputerAccess computer) {

		}

		@Optional.Method(modid = "computercraft")
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
				return tother.getWorld().equals(te.getWorld())
						&& tother.getPos().equals(te.getPos());
			}

			return false;
		}

	}

	/*
	 * end
	 */

}

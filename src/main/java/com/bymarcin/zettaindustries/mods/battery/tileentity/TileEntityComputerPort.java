package com.bymarcin.zettaindustries.mods.battery.tileentity;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
        @Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers"),
        @Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = "ComputerCraft")
})
public class TileEntityComputerPort extends BasicRectangularMultiblockTileEntityBase implements SimpleComponent /*, IPeripheralProvider*/ {

    @Override
    public void isGoodForFrame() throws MultiblockValidationException {
        throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
    }

    @Override
    public void isGoodForSides() throws MultiblockValidationException {
    }

    @Override
    public void isGoodForTop() throws MultiblockValidationException {
        throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
    }

    @Override
    public void isGoodForBottom() throws MultiblockValidationException {
        throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
    }

    @Override
    public void isGoodForInterior() throws MultiblockValidationException {
        throw new MultiblockValidationException(String.format("%d, %d, %d - Controller may only be placed in the battery side", this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
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

    private BatteryController getControler() {
        return (BatteryController) getMultiblockController();
    }

    private TileEntityPowerTap findPowerTap(int id) {
        if (getControler() != null) {
            TileEntityPowerTap[] array = getControler().getPowerTaps().toArray(new TileEntityPowerTap[]{});
            if (id >= 0 && id < array.length) {
                return array[id];
            }
        }
        return null;
    }

    private TileEntityPowerTap findPowerTap(String label) {
        if (getControler() != null) {
            TileEntityPowerTap[] array = getControler().getPowerTaps().toArray(new TileEntityPowerTap[]{});
            for (int i = 0; i < array.length; i++) {
                if (array[i].getLabel().equals(label)) {
                    return array[i];
                }
            }
        }
        return null;
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] setIn(Context c, Arguments args) {
        String name = null;
        int id = -1;
        if (args.isString(0)) {
            name = args.checkString(0);
        } else {
            id = args.checkInteger(0);
        }

        TileEntityPowerTap powerTap = name != null ? findPowerTap(name) : findPowerTap(id);
        if (powerTap != null) {
            powerTap.setIn();
            return null;
        } else {
            return new Object[]{null, "Electrode or Controller not found."};
        }
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] setOut(Context c, Arguments args) {
        String name = null;
        int id = -1;
        if (args.isString(0)) {
            name = args.checkString(0);
        } else {
            id = args.checkInteger(0);
        }

        TileEntityPowerTap powerTap = name != null ? findPowerTap(name) : findPowerTap(id);
        if (powerTap != null) {
            powerTap.setOut();
            return null;
        } else {
            return new Object[]{null, "Electrode or Controller not found."};
        }
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] getEnergyStored(Context c, Arguments args) {
        if (getControler() != null)
            return new Object[]{getControler().getStorage().getRealEnergyStored()};
        return new Object[]{null, "Controller block not found. Rebuild your battery."};
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] getMaxEnergyStored(Context c, Arguments args) {
        if (getControler() != null)
            return new Object[]{getControler().getStorage().getRealMaxEnergyStored()};
        return new Object[]{null, "Controler block not found. Rebuild your battery."};
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] setElectrodeTransfer(Context c, Arguments args) {
        String label = null;
        int id = -1;
        if (args.isString(0)) {
            label = args.checkString(0);
        } else {
            id = args.checkInteger(0);
        }

        int transfer = args.checkInteger(1);
        TileEntityPowerTap powerTap = label != null ? findPowerTap(label) : findPowerTap(id);
        if (powerTap != null) {
            powerTap.setTransfer(transfer);
            return null;
        } else {
            return new Object[]{null, "Electrode or Controller not found."};
        }
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] setAllElectrodeTransfer(Context c, Arguments args) {
        int transfer = args.checkInteger(0);
        if (getControler() != null) {
            for (TileEntityPowerTap tap : getControler().getPowerTaps())
                tap.setTransfer(transfer);
            return null;
        }
        return new Object[]{null, "Controller block not found. Rebuild your battery."};
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] getMaxElectrodeTransfer(Context c, Arguments args) {
        if (getControler() != null)
            return new Object[]{getControler().getStorage().getMaxExtract()};
        return new Object[]{null, "Controller block not found. Rebuild your battery."};
    }

    @Callback
    @Optional.Method(modid = "OpenComputers")
    public Object[] getEnergyBalanceLastTick(Context c, Arguments args) {
        if (getControler() != null)
            return new Object[]{getControler().getLastTickBalance()};
        return new Object[]{null, "Controller block not found. Rebuild your battery."};
    }

    // ComputerCraft

//	@Optional.Method(modid = "ComputerCraft")
//	@Override
//	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
//		TileEntity te = world.getTileEntity(x, y, z);
//		if(te instanceof TileEntityComputerPort)
//			return new BatteryPeripheral((TileEntityComputerPort) te);
//		else
//			return null;
//	}

//	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft")
//	public static class BatteryPeripheral implements IPeripheral {
//
//		TileEntityComputerPort te;
//
//		public BatteryPeripheral(TileEntityComputerPort te) {
//			this.te = te;
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public String getType() {
//			return "big_battery";
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public String[] getMethodNames() {
//			return new String[] {
//					"getEnergyBalanceLastTick",
//					"getEnergyStored",
//					"getMaxElectrodeTransfer",
//					"getMaxEnergyStored",
//					"setAllElectrodeTransfer",
//					"setElectrodeTransfer",
//					"setIn",
//					"setOut"
//			};
//		}
//
//		@Optional.Method(modid = "ComputerCraft")
//		@Override
//		public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
//
//			switch (method) {
//			case 0: // getEnergyBalanceLastTick
//				if (te.getControler() != null)
//					return new Object[] { te.getControler().getLastTickBalance() };
//				return new Object[] { null, "Controller block not found. Rebuild your battery." };
//			case 1: // getEnergyStored
//				if (te.getControler() != null)
//					return new Object[] { te.getControler().getStorage().getRealEnergyStored() };
//				return new Object[] { null, "Controller block not found. Rebuild your battery." };
//			case 2: // getMaxElectrodeTransfer
//				if (te.getControler() != null)
//					return new Object[] { te.getControler().getStorage().getMaxExtract() };
//				return new Object[] { null, "Controller block not found. Rebuild your battery." };
//			case 3: // getMaxEnergyStored
//				if (te.getControler() != null)
//					return new Object[] { te.getControler().getStorage().getRealMaxEnergyStored() };
//				return new Object[] { null, "Controler block not found. Rebuild your battery." };
//			case 4: // setAllElectrodeTransfer
//				if (!(arguments != null && arguments.length > 0 && arguments[0] instanceof Double)) {
//					return new Object[] { null, "Wrong argument" };
//				}
//				int transfer = ((Double) arguments[0]).intValue();
//				if (te.getControler() != null) {
//					for (TileEntityPowerTap tap : te.getControler().getPowerTaps())
//						tap.setTransfer(transfer);
//					return null;
//				}
//				return new Object[] { null, "Controller block not found. Rebuild your battery." };
//			case 5: // setElectrodeTransfer
//				if (!(arguments != null && arguments.length > 1 && arguments[1] instanceof Double && (arguments[0] instanceof Double || arguments[0] instanceof String))) {
//					return new Object[] { null, "Wrong arguments" };
//				}
//
//				String label = null;
//				int id = -1;
//				if (arguments[0] instanceof String) {
//					label = (String) arguments[0];
//				} else {
//					id = ((Double) arguments[0]).intValue();
//				}
//
//				TileEntityPowerTap powerTap = label != null ? te.findPowerTap(label) : te.findPowerTap(id);
//				if (powerTap != null) {
//					powerTap.setTransfer(((Double) arguments[1]).intValue());
//					return null;
//				} else {
//					return new Object[] { null, "Electrode or Controller not found." };
//				}
//			case 6: // setIn
//				if (!(arguments != null && arguments.length > 1 && arguments[1] instanceof Double && (arguments[0] instanceof Double || arguments[0] instanceof String))) {
//					return new Object[] { null, "Wrong arguments" };
//				}
//				String name = null;
//				int eid = -1;
//				if (arguments[0] instanceof String) {
//					name = (String) arguments[0];
//				} else {
//					eid = ((Double) arguments[0]).intValue();
//				}
//
//				TileEntityPowerTap powerTapIN = name != null ? te.findPowerTap(name) : te.findPowerTap(eid);
//				if (powerTapIN != null) {
//					powerTapIN.setIn();
//					return null;
//				} else {
//					return new Object[] { null, "Electrode or Controller not found." };
//				}
//			case 7: // setOut
//				if (!(arguments != null && arguments.length > 1 && arguments[1] instanceof Double && (arguments[0] instanceof Double || arguments[0] instanceof String))) {
//					return new Object[] { null, "Wrong arguments" };
//				}
//				String nameOut = null;
//				int oid = -1;
//				if (arguments[0] instanceof String) {
//					nameOut = (String) arguments[0];
//				} else {
//					oid = ((Double) arguments[0]).intValue();
//				}
//
//				TileEntityPowerTap powerTapOUT = nameOut != null ? te.findPowerTap(nameOut) : te.findPowerTap(oid);
//				if (powerTapOUT != null) {
//					powerTapOUT.setOut();
//					return null;
//				} else {
//					return new Object[] { null, "Electrode or Controller not found." };
//				}
//			}
//			return null;
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
//	}

}

package com.bymarcin.zettaindustries.mods.mgc;

import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.util.VecInt;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;

import net.minecraft.world.World;

public class DriverElectricConductor extends DriverTileEntity {

	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
		return new Environment((IElectricTile) world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass() {
		return IElectricTile.class;
	}

	public static final class Environment extends ManagedTileEntityEnvironment<IElectricTile> {
		public Environment(final IElectricTile tileEntity) {
			super(tileEntity, "mgc_electric");
		}
		
		@Callback
		public Object[] getConductors(final Context context, final Arguments args) {
			return new Object[]{tileEntity.getConds(VecInt.NULL_VECTOR, args.checkInteger(0))};
		}

	}
}

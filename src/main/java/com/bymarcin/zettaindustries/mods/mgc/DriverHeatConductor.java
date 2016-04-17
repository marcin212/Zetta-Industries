package com.bymarcin.zettaindustries.mods.mgc;

import com.cout970.magneticraft.api.heat.IHeatTile;
import com.cout970.magneticraft.api.util.VecInt;

import net.minecraft.world.World;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;

public class DriverHeatConductor extends DriverTileEntity{

	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
		return new Environment((IHeatTile)world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass() {
		return IHeatTile.class;
	}
	
	public static final class Environment extends ManagedTileEntityEnvironment<IHeatTile> {
		   public Environment(final IHeatTile tileEntity) {
	            super(tileEntity, "mgc_heat");
	        }
		   
			@Callback
			public Object[] getHeatConductors(final Context context, final Arguments args) {
				return new Object[]{tileEntity.getHeatCond(VecInt.NULL_VECTOR)};
			}
	}

}

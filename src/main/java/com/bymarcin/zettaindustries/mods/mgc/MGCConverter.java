package com.bymarcin.zettaindustries.mods.mgc;

import java.util.Map;

import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.heat.IHeatConductor;

import li.cil.oc.api.driver.Converter;

public class MGCConverter implements Converter {

	@Override
	public void convert(Object value, Map<Object, Object> output) {
		 if (value instanceof IElectricConductor) {
			output.put("voltage", ((IElectricConductor) value).getVoltage());
			output.put("intensity", ((IElectricConductor) value).getIntensity());
			output.put("resistance", ((IElectricConductor) value).getResistance());
			output.put("storage", ((IElectricConductor) value).getStorage());
			output.put("tier", ((IElectricConductor) value).getTier());
		 }
		 
		 if (value instanceof IHeatConductor) {
			output.put("temperature", ((IHeatConductor) value).getTemperature());
			output.put("maxtemperature", ((IHeatConductor) value).getMaxTemp());
			output.put("resistance", ((IHeatConductor) value).getResistance());
		 }
	}

}

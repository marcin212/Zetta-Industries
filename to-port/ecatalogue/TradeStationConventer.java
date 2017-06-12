package com.bymarcin.zettaindustries.mods.ecatalogue;

import java.util.Map;

import li.cil.oc.api.driver.Converter;
import forestry.api.mail.ITradeStation;
import forestry.api.mail.ITradeStationInfo;

public class TradeStationConventer implements Converter{

	@Override
	public void convert(Object obj, Map<Object, Object> result) {
		if(obj instanceof ITradeStation){
			ITradeStation trade = (ITradeStation) obj;
			ITradeStationInfo info = trade.getTradeInfo();
			result.put("required", info.getRequired());
			result.put("tradegood", info.getTradegood());
			result.put("state", info.getState().isOk());
			result.put("owner", info.getOwner().getName());
			result.put("address", info.getAddress().getName());
			result.put("type", info.getAddress().getType().toString());
		}
	}

}

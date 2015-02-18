package com.bymarcin.zettaindustries.mods.ecatalogue;

import java.util.Map;

import li.cil.oc.api.driver.Converter;
import forestry.api.mail.ITradeStation;
import forestry.api.mail.TradeStationInfo;

public class TradeStationConventer implements Converter{

	@Override
	public void convert(Object obj, Map<Object, Object> result) {
		if(obj instanceof ITradeStation){
			ITradeStation trade = (ITradeStation) obj;
			TradeStationInfo info = trade.getTradeInfo();
			result.put("required", info.required);
			result.put("tradegood", info.tradegood);
			result.put("state", info.state.isOk());
			result.put("owner", info.owner.getName());
			result.put("address", info.address.getName());
			result.put("type", info.address.getType().toString());
		}
	}

}

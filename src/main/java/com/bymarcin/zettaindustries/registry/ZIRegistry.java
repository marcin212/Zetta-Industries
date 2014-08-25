package com.bymarcin.zettaindustries.registry;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

public class ZIRegistry {
	protected static Set<IProxy> proxy = new HashSet<IProxy>();
	
	public static void registerProxy(IProxy proxy){
		ZIRegistry.proxy.add(Preconditions.checkNotNull(proxy));
	}
	
}

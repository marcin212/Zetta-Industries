package com.bymarcin.zettaindustries.registry;


public class ClientProxy extends Proxy{

	@Override
	public void init() {
		 for(IProxy p: proxy)
			 p.clientSide();
	}
	
}

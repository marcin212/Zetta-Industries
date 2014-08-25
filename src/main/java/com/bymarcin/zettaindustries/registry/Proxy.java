package com.bymarcin.zettaindustries.registry;

public class Proxy extends ZIRegistry{

	public void init(){
		 for(IProxy p: proxy)
			 p.serverSide();
	}
	
}

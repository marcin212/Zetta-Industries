package com.bymarcin.zettaindustries.utils;

public class Avg {
	int size=10;
	long[] tab= new long[size];
	int index = 0;

	public void putValue(long lastRecive){
		tab[index] = lastRecive;
		index = (index+1) % tab.length;
	}
	
	public float getAvg(){
		long sum = 0;
		for(long i: tab)
			sum += i;
		return (float)sum/(float)tab.length;
	}	
}

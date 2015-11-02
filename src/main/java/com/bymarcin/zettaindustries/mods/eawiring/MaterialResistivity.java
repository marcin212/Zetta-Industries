package com.bymarcin.zettaindustries.mods.eawiring;

public class MaterialResistivity {
	public static final double SILVER = 1.59e-8;
	public static final double COPPER = 1.72e-8;
	public static final double GOLD = 2.44e-8;
	public static final double ALUMINIUM = 2.82e-8;
	public static final double TUNGSTEN = 5.60e-8;
	public static final double NICKEL = 6.99e-8;
	public static final double IRON = 9.8e-8;
	public static final double TIN = 10.9e-8;
	public static final double PLATINUM = 11e-8;
	public static final double LEAD = 22e-8;
	public static final double NICHROME = 150e-8;
	public static final double COAL = 3.5e-5;
	public static final double GERMAN = 0.46;
	public static final double SILICON = 640;
	public static final double GLASS = 10e12;
	public static final double RUBBER = 10e13;
	public static final double SULFUR = 10e15;
	public static final double BRASS = 7.5e-8;
	public static final double CASTIRON = 4e-6;
	
	public static double getMaterialResistivityPerBlock(double materialResistivity, double diameter){
		return materialResistivity/( (diameter/2)*(diameter/2)*Math.PI );
	}
	
	
}

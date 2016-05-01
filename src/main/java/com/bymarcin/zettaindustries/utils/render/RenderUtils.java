package com.bymarcin.zettaindustries.utils.render;

public class RenderUtils {
	//public static final ForgeDirection[] FORGE_DIRECTIONS = {ForgeDirection.NORTH,  ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
	
	public static float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}
	
}

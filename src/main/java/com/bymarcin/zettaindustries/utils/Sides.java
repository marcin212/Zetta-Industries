package com.bymarcin.zettaindustries.utils;


public enum Sides {
	
	BOTTOM("bottom"),TOP("top"),BACK("back"),FRONT("front"),RIGHT("right"),LEFT("left");
	
	public String name;
	Sides(String name){
		this.name = name;
	}
	
//	public ForgeDirection getForgeDirection(ForgeDirection front){
//			switch(this){
//				case BACK: return ForgeDirection.getOrientation(front.ordinal()).getOpposite();
//				case FRONT: return ForgeDirection.getOrientation(front.ordinal());
//				case RIGHT: return ForgeDirection.getOrientation(front.ordinal()%5+2);
//				case LEFT: return ForgeDirection.getOrientation(front.ordinal()%5+2).getOpposite();
//				case TOP:
//				case BOTTOM: return ForgeDirection.getOrientation(ordinal());
//			}
//			return front;
//	}	
//	
//	public static final ForgeDirection neighborsBySide[][] = new ForgeDirection[][] {
//		{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST},
//		{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST},
//		{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST, ForgeDirection.WEST},
//		{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.WEST, ForgeDirection.EAST},
//		{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH},
//		{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.SOUTH, ForgeDirection.NORTH}
//	};
}

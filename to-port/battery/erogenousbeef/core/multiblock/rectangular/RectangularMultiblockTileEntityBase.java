package com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.rectangular;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.common.CoordTriplet;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockTileEntityBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import net.minecraft.util.EnumFacing;

public abstract class RectangularMultiblockTileEntityBase extends
		MultiblockTileEntityBase {

	PartPosition position;
	EnumFacing outwards;
	
	public RectangularMultiblockTileEntityBase() {
		super();
		
		position = PartPosition.Unknown;
		outwards = EnumFacing.NORTH;
	}

	// Positional Data
	public EnumFacing getOutwardsDir() {
		return outwards;
	}
	
	public PartPosition getPartPosition() {
		return position;
	}

	// Handlers from MultiblockTileEntityBase 
	@Override
	public void onAttached(MultiblockControllerBase newController) {
		super.onAttached(newController);
		recalculateOutwardsDirection(newController.getMinimumCoord(), newController.getMaximumCoord());
	}
	
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
		CoordTriplet maxCoord = controller.getMaximumCoord();
		CoordTriplet minCoord = controller.getMinimumCoord();
		
		// Discover where I am on the reactor
		recalculateOutwardsDirection(minCoord, maxCoord);
	}

	@Override
	public void onMachineBroken() {
		position = PartPosition.Unknown;
		outwards = EnumFacing.NORTH;
	}
	
	// Positional helpers
	public void recalculateOutwardsDirection(CoordTriplet minCoord, CoordTriplet maxCoord) {
		outwards = EnumFacing.NORTH;
		position = PartPosition.Unknown;

		int facesMatching = 0;
		if(maxCoord.x == this.pos.getX() || minCoord.x == this.pos.getX()) { facesMatching++; }
		if(maxCoord.y == this.pos.getY() || minCoord.y == this.pos.getY()) { facesMatching++; }
		if(maxCoord.z == this.pos.getZ() || minCoord.z == this.pos.getZ()) { facesMatching++; }
		
		if(facesMatching <= 0) { position = PartPosition.Interior; }
		else if(facesMatching >= 3) { position = PartPosition.FrameCorner; }
		else if(facesMatching == 2) { position = PartPosition.Frame; }
		else {
			// 1 face matches
			if(maxCoord.x == this.pos.getX()) {
				position = PartPosition.EastFace;
				outwards = EnumFacing.EAST;
			}
			else if(minCoord.x == this.pos.getX()) {
				position = PartPosition.WestFace;
				outwards = EnumFacing.WEST;
			}
			else if(maxCoord.z == this.pos.getZ()) {
				position = PartPosition.SouthFace;
				outwards = EnumFacing.SOUTH;
			}
			else if(minCoord.z == this.pos.getZ()) {
				position = PartPosition.NorthFace;
				outwards = EnumFacing.NORTH;
			}
			else if(maxCoord.y == this.pos.getY()) {
				position = PartPosition.TopFace;
				outwards = EnumFacing.UP;
			}
			else {
				position = PartPosition.BottomFace;
				outwards = EnumFacing.DOWN;
			}
		}
	}
	
	///// Validation Helpers (IMultiblockPart)
	public abstract void isGoodForFrame() throws MultiblockValidationException;

	public abstract void isGoodForSides() throws MultiblockValidationException;

	public abstract void isGoodForTop() throws MultiblockValidationException;

	public abstract void isGoodForBottom() throws MultiblockValidationException;

	public abstract void isGoodForInterior() throws MultiblockValidationException;
}

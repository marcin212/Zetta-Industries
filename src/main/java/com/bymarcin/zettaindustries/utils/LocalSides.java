package com.bymarcin.zettaindustries.utils;

import com.bymarcin.zettaindustries.utils.math.Matrix4f;
import com.bymarcin.zettaindustries.utils.math.Vector3f;
import com.bymarcin.zettaindustries.utils.render.RenderUtils;

public enum LocalSides {
	NORTH,
	EAST,
	SOUTH,
	WEST;
	

	private static Vector3f vec = new Vector3f(0, 1, 0);

	public LocalSides nextSide() {
		switch (this) {
		case NORTH:
			return LocalSides.EAST;
		case EAST:
			return LocalSides.SOUTH;
		case SOUTH:
			return LocalSides.WEST;
		case WEST:
			return LocalSides.NORTH;
		}
		return LocalSides.NORTH;
	}


	public void rotateMatrix(Matrix4f matrix) {
		switch (this) {
		case NORTH:
			break;
		case EAST:
			matrix.rotate((float) RenderUtils.angle90, vec);
			break;
		case SOUTH:
			matrix.rotate((float) RenderUtils.angle180, vec);
			break;
		case WEST:
			matrix.rotate((float) RenderUtils.angle270, vec);
			break;
		}
	}
}

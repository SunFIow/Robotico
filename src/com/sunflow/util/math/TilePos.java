package com.sunflow.util.math;

import com.sunflow.math.SVector;

public class TilePos extends SVector {
	public TilePos(int x, int y) { super(x, y); }

	public TilePos(double x, double y) { super((int) x, (int) y); }

	@Override
	public int getX() { return (int) this.x; }

	@Override
	public int getY() { return (int) this.y; }
}

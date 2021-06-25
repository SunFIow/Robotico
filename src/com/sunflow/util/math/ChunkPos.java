package com.sunflow.util.math;

public class ChunkPos {
	/** Value representing an absent or invalid chunkpos */
	public static final long SENTINEL = asLong(1875016, 1875016);
	public final int x;
	public final int y;

	public ChunkPos(int x, int z) {
		this.x = x;
		this.y = z;
	}

	public ChunkPos(TilePos pos) {
		this.x = pos.getX() >> 4;
		this.y = pos.getY() >> 4;
	}

	public ChunkPos(long longIn) {
		this.x = (int) longIn;
		this.y = (int) (longIn >> 32);
	}

	public long asLong() {
		return asLong(this.x, this.y);
	}

	/** Converts the chunk coordinate pair to a long */
	public static long asLong(int x, int y) {
		return x & 4294967295L | (y & 4294967295L) << 32;
	}

	public static int getX(long chunkAsLong) {
		return (int) (chunkAsLong & 4294967295L);
	}

	public static int getY(long chunkAsLong) {
		return (int) (chunkAsLong >>> 32 & 4294967295L);
	}

}

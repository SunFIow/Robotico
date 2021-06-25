package com.sunflow.world.chunk;

import com.sunflow.tile.TileState;
import com.sunflow.util.math.TilePos;

public class Chunk implements IChunk {
	public static byte SIZE_X = 16, SIZE_Y = 16;
	private TileState[][] tiles;

	public Chunk() {
		tiles = new TileState[Chunk.SIZE_X][Chunk.SIZE_Y];
	}

	public TileState getTileState(TilePos pos) {
		int x = pos.getX() & 15;
		int y = pos.getY() & 15;
		return tiles[x][y];
	}

	public TileState getTileState(int x, int y) {
		x = x & 15;
		y = y & 15;
		return tiles[x][y];
	}

	public void setTileState(TilePos pos, TileState state) {
		int x = pos.getX() & 15;
		int y = pos.getY() & 15;
//		tiles[x][y] = state;
		setTileStateAt(x, y, state);
	}

	public void setTileStateAt(int x, int y, TileState state) {
		tiles[x][y] = state;
	}
}

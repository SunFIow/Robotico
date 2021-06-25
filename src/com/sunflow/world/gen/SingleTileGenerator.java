package com.sunflow.world.gen;

import com.sunflow.tile.Tile;
import com.sunflow.tile.TileState;
import com.sunflow.world.chunk.Chunk;
import com.sunflow.world.chunk.ChunkHolder;
import com.sunflow.world.chunk.ChunkStatus;

public class SingleTileGenerator implements ChunkGenerator {
	private final Tile tile;

	public SingleTileGenerator(Tile tile) { this.tile = tile; }

	@Override
	public Chunk generateChunk(ChunkHolder chunkHolder, ChunkStatus status) {
		Chunk chunk = new Chunk();

		for (int x = 0; x < Chunk.SIZE_X; x++) for (int y = 0; y < Chunk.SIZE_Y; y++) {
			TileState state = new TileState();
			state.setTile(tile);
			chunk.setTileStateAt(x, y, state);
		}

		return chunk;
	}
}

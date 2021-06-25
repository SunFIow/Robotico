package com.sunflow.world;

import com.sunflow.tile.TileState;
import com.sunflow.util.math.TilePos;
import com.sunflow.world.chunk.Chunk;
import com.sunflow.world.chunk.ChunkProvider;
import com.sunflow.world.chunk.ChunkStatus;
import com.sunflow.world.chunk.IChunk;
import com.sunflow.world.gen.ChunkGenerator;

public class World {

	private ChunkProvider chunkProvider;

	public World(ChunkGenerator generator) {
		this.chunkProvider = new ChunkProvider(this, generator);
	}

	public TileState getTileState(TilePos pos) {
		Chunk chunk = this.getChunk(pos);
		return chunk.getTileState(pos);
	}

	public TileState getTileState(int x, int y) {
		Chunk chunk = this.getChunk(x, y);
		return chunk.getTileState(x, y);
	}

	public Chunk getChunk(TilePos pos) {
//		return this.getChunk(pos.getX() / Chunk.SIZE_X, pos.getY() / Chunk.SIZE_Y);
		return this.getChunk(pos.getX() >> 4, pos.getY() >> 4);
	}

	public Chunk getChunk(int chunkX, int chunkY) {
		return (Chunk) this.getChunk(chunkX, chunkY, ChunkStatus.FULL, true);
	}

	public IChunk getChunk(int x, int y, ChunkStatus requiredStatus, boolean nonnull) {
		IChunk ichunk = this.getChunkProvider().getChunk(x, y, requiredStatus, nonnull);
		if (ichunk == null && nonnull) {
			throw new IllegalStateException("Should always be able to create a chunk!");
		} else {
			return ichunk;
		}
	}

	private ChunkProvider getChunkProvider() { return this.chunkProvider; }
}

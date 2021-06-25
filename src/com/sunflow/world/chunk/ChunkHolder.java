package com.sunflow.world.chunk;

import com.sunflow.util.math.ChunkPos;

public class ChunkHolder {
	private IChunk chunk;
//	private final ChunkPos pos;

	public ChunkHolder(ChunkPos chunkPos) {
//		this.pos = chunkPos;
	}

	public void setChunk(IChunk chunk) {
		this.chunk = chunk;
	}

	public IChunk getChunk() {
		return this.chunk;
	}

	public IChunk getOrCreateChunk(ChunkManager manager, ChunkStatus status) {
		if (this.chunk == null) manager.generateChunk(this, status);
		return this.chunk;
	}
}

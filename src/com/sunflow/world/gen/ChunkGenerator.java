package com.sunflow.world.gen;

import com.sunflow.world.chunk.ChunkHolder;
import com.sunflow.world.chunk.ChunkStatus;
import com.sunflow.world.chunk.IChunk;

public interface ChunkGenerator {

	public IChunk generateChunk(ChunkHolder chunkHolder, ChunkStatus status);
}

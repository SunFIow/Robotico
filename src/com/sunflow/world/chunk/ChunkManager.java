package com.sunflow.world.chunk;

import java.util.HashMap;

import com.sunflow.util.math.ChunkPos;
import com.sunflow.world.gen.ChunkGenerator;

public class ChunkManager {

	private final HashMap<Long, ChunkHolder> loadedChunks = new HashMap<>();
	private final ChunkGenerator generator;

	public ChunkManager(ChunkGenerator generator) {
		this.generator = generator;
	}

	protected ChunkHolder getChunkHolder(long chunkPosIn) {
		ChunkHolder holder = this.loadedChunks.get(chunkPosIn);
		if (holder == null) {
			holder = new ChunkHolder(new ChunkPos(chunkPosIn));
			setChunkHolder(chunkPosIn, holder);
		}
		return holder;
	}

	private void setChunkHolder(long chunkPosIn, ChunkHolder holder) {
		this.loadedChunks.put(chunkPosIn, holder);
	}

	public void generateChunk(ChunkHolder chunkHolder, ChunkStatus status) {
		chunkHolder.setChunk(generator.generateChunk(chunkHolder, status));
	}

}

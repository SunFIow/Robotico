package com.sunflow.world.chunk;

import com.sunflow.util.math.ChunkPos;
import com.sunflow.world.World;
import com.sunflow.world.gen.ChunkGenerator;

public class ChunkProvider {

//	private final World world;
	public final ChunkManager chunkManager;

	private final long[] recentPositions = new long[4];
	private final ChunkStatus[] recentStatuses = new ChunkStatus[4];
	private final IChunk[] recentChunks = new IChunk[4];

	public ChunkProvider(World world, ChunkGenerator generator) {
//		this.world = world;
		this.chunkManager = new ChunkManager(generator);
	}

	public IChunk getChunk(int chunkX, int chunkY, ChunkStatus requiredStatus, boolean load) {
		long chunkPos = ChunkPos.asLong(chunkX, chunkY);

		for (int j = 0; j < 4; ++j) {
			if (chunkPos == this.recentPositions[j] && requiredStatus == this.recentStatuses[j]) {
				IChunk ichunk = this.recentChunks[j];
				if (ichunk != null || !load) {
					return ichunk;
				}
			}
		}

		ChunkHolder chunkholder = this.chunkManager.getChunkHolder(chunkPos);
		IChunk ichunk = chunkholder.getOrCreateChunk(this.chunkManager, requiredStatus);

		this.cacheChunk(chunkPos, ichunk, requiredStatus);

		return ichunk;
	}

	private void cacheChunk(long pos, IChunk chunk, ChunkStatus status) {
		for (int i = 3; i > 0; --i) {
			this.recentPositions[i] = this.recentPositions[i - 1];
			this.recentStatuses[i] = this.recentStatuses[i - 1];
			this.recentChunks[i] = this.recentChunks[i - 1];
		}

		this.recentPositions[0] = pos;
		this.recentStatuses[0] = status;
		this.recentChunks[0] = chunk;
	}
}

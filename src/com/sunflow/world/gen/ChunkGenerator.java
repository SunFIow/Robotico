package com.sunflow.world.gen;

import com.sunflow.world.World;
import com.sunflow.world.biome.provider.BiomeProvider;
import com.sunflow.world.chunk.ChunkHolder;
import com.sunflow.world.chunk.ChunkStatus;
import com.sunflow.world.chunk.IChunk;

public abstract class ChunkGenerator<C extends ChunkGenerationSettings> {

	protected final World world;
	protected final BiomeProvider biomeProvider;
	protected final C settings;

	public ChunkGenerator(World world, BiomeProvider biomeProvider, C generationSettings) {
		this.world = world;
		this.biomeProvider = biomeProvider;
		this.settings = generationSettings;
	}

	public abstract IChunk generateChunk(ChunkHolder chunkHolder, ChunkStatus status);
}

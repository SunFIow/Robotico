package com.sunflow.world.gen;

import com.sunflow.world.World;
import com.sunflow.world.biome.provider.BiomeProvider;

public interface IChunkGeneratorFactory<C extends ChunkGenerationSettings, T extends ChunkGenerator<C>> {
	T create(World world, BiomeProvider biomeProvider, C settings);
}
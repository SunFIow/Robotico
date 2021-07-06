package com.sunflow.world.gen;

import java.util.function.Supplier;

import com.sunflow.world.World;
import com.sunflow.world.biome.provider.BiomeProvider;

public class ChunkGeneratorType<C extends ChunkGenerationSettings, T extends ChunkGenerator<C>> {
	public static final ChunkGeneratorType<DefaultChunkGenerator.Settings, DefaultChunkGenerator> DEFAULT = register("default", DefaultChunkGenerator::new, DefaultChunkGenerator.Settings::new);

	private static <C extends ChunkGenerationSettings, T extends ChunkGenerator<C>> ChunkGeneratorType<C, T> register(String key, IChunkGeneratorFactory<C, T> factory, Supplier<C> settings) {
		ChunkGeneratorType<C, T> type = new ChunkGeneratorType<>(factory, settings);
		return type;
	}

	private final IChunkGeneratorFactory<C, T> factory;
	private final Supplier<C> settings;

	public ChunkGeneratorType(IChunkGeneratorFactory<C, T> factory, Supplier<C> settings) {
		this.factory = factory;
		this.settings = settings;
	}

	public T create(World world, BiomeProvider biomeProvider, C settings) {
		return this.factory.create(world, biomeProvider, settings);
	}

	public C createSettings() {
		return this.settings.get();
	}

}

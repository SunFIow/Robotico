package com.sunflow.world.biome.provider;

import java.util.function.Function;

import com.sunflow.world.storage.WorldInfo;

public class BiomeProviderType<C extends BiomeProviderSettings, T extends BiomeProvider> {
	public static final BiomeProviderType<SingleBiomeProvider.Settings, SingleBiomeProvider> SINGLE = register("single", SingleBiomeProvider::new, SingleBiomeProvider.Settings::new);

	private static <C extends BiomeProviderSettings, T extends BiomeProvider> BiomeProviderType<C, T> register(String key, Function<C, T> factory, Function<WorldInfo, C> settingsFactory) {
		BiomeProviderType<C, T> type = new BiomeProviderType<>(factory, settingsFactory);
		return type;
	}

	private final Function<C, T> factory;
	private final Function<WorldInfo, C> settingsFactory;

	public BiomeProviderType(Function<C, T> factory, Function<WorldInfo, C> settingsFactory) {
		this.factory = factory;
		this.settingsFactory = settingsFactory;
	}

	public T create(C settings) {
		return this.factory.apply(settings);
	}

	public C createSettings(WorldInfo worldInfo) {
		return this.settingsFactory.apply(worldInfo);
	}

}

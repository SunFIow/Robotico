package com.sunflow.world.biome.provider;

import com.sunflow.world.storage.WorldInfo;

public class SingleBiomeProvider extends BiomeProvider {
	public SingleBiomeProvider(Settings settings) {}

	public static class Settings extends BiomeProviderSettings {

		public Settings(WorldInfo worldInfo) { super(worldInfo); }

	}
}

package net.minecraft.util.registry;

import com.mojang.serialization.Lifecycle;

import net.minecraft.util.registry.Registry.RegistryKey;

public class GameData {

//	public static <T extends Registry<T>> IForgeRegistryEntry<T> getWrapper(RegistryKey<? extends Registry<T>> key, Lifecycle cycle, String defKey) {
//		return null;
//	}

	public static <T> MutableRegistry getWrapper(RegistryKey<? extends Registry<T>> key, Lifecycle cycle, String defKey) {
		return null;
	}

}

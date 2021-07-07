package net.minecraft.util.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import com.sunflow.api.distmarker.Dist;
import com.sunflow.api.distmarker.OnlyIn;
import com.sunflow.logging.LogManager;
import com.sunflow.logging.SunLogger;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public class SimpleRegistry<T> extends MutableRegistry<T> {
	protected static final SunLogger LOGGER0 = LogManager.getLogger();
	private final BiMap<ResourceLocation, T> registryObjects;
	private final BiMap<RegistryKey<T>, T> keyToObjectMap;
	private final Map<T, Lifecycle> objectToLifecycleMap;
	private Lifecycle lifecycle;
	protected Object[] values;
	private int nextFreeId;

	public SimpleRegistry(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
		super(registryKey, lifecycle);
		this.registryObjects = HashBiMap.create();
		this.keyToObjectMap = HashBiMap.create();
		this.objectToLifecycleMap = Maps.newIdentityHashMap();
		this.lifecycle = lifecycle;
	}

	@Override
	public <V extends T> V register(int id, RegistryKey<T> name, V instance, Lifecycle lifecycle) {
		return this.register(id, name, instance, lifecycle, true);
	}

	private <V extends T> V register(int index, RegistryKey<T> registryKey, V value, Lifecycle lifecycle, boolean logDuplicateKeys) {
		Validate.notNull(registryKey);
		Validate.notNull((T) value);

		this.values = null;
		if (logDuplicateKeys && this.keyToObjectMap.containsKey(registryKey)) {
			LOGGER0.debug("Adding duplicate key '{}' to registry", (Object) registryKey);
		}

		if (this.registryObjects.containsValue(value)) {
			LOGGER0.error("Adding duplicate value '{}' to registry", value);
		}

		this.registryObjects.put(registryKey.getLocation(), value);
		this.keyToObjectMap.put(registryKey, value);
		this.objectToLifecycleMap.put(value, lifecycle);
		this.lifecycle = this.lifecycle.add(lifecycle);
		if (this.nextFreeId <= index) {
			this.nextFreeId = index + 1;
		}

		return value;
	}

	@Override
	public <V extends T> V register(RegistryKey<T> name, V instance, Lifecycle lifecycle) {
		return this.register(this.nextFreeId, name, instance, lifecycle);
	}

	@Override
	public <V extends T> V validateAndRegister(OptionalInt index, RegistryKey<T> registryKey, V value, Lifecycle lifecycle) {
		Validate.notNull(registryKey);
		Validate.notNull((T) value);
		T t = this.keyToObjectMap.get(registryKey);
		int i = -1;
		if (t == null) {
			i = index.isPresent() ? index.getAsInt() : this.nextFreeId;
		} else {
			if (index.isPresent() && index.getAsInt() != i) {
				throw new IllegalStateException("ID mismatch");
			}
			this.objectToLifecycleMap.remove(t);
		}

		return this.register(i, registryKey, value, lifecycle, false);
	}

	/**
	 * Gets the name we use to identify the given object.
	 */
	@Nullable
	public ResourceLocation getKey(T value) {
		return this.registryObjects.inverse().get(value);
	}

	public Optional<RegistryKey<T>> getOptionalKey(T value) {
		return Optional.ofNullable(this.keyToObjectMap.inverse().get(value));
	}

	@Nullable
	public T getValueForKey(@Nullable RegistryKey<T> key) {
		return this.keyToObjectMap.get(key);
	}

	public Lifecycle getLifecycleByRegistry(T object) {
		return this.objectToLifecycleMap.get(object);
	}

	public Lifecycle getLifecycle() {
		return this.lifecycle;
	}

	@Nullable
	public T getOrDefault(@Nullable ResourceLocation name) {
		return this.registryObjects.get(name);
	}

	/**
	 * Gets all the keys recognized by this registry.
	 */
	public Set<ResourceLocation> keySet() {
		return Collections.unmodifiableSet(this.registryObjects.keySet());
	}

	public Set<Map.Entry<RegistryKey<T>, T>> getEntries() {
		return Collections.unmodifiableMap(this.keyToObjectMap).entrySet();
	}

	@Nullable
	public T getRandom(Random random) {
		if (this.values == null) {
			Collection<?> collection = this.registryObjects.values();
			if (collection.isEmpty()) {
				return null;
			}

			this.values = collection.toArray(new Object[collection.size()]);
		}

		return Util.getRandomObject((T[]) this.values, random);
	}

	@OnlyIn(Dist.CLIENT)
	public boolean containsKey(ResourceLocation name) {
		return this.registryObjects.containsKey(name);
	}

	public static class Entry<T> {
		public final RegistryKey<T> name;
		public final int index;
		public final T value;

		public Entry(RegistryKey<T> name, int index, T value) {
			this.name = name;
			this.index = index;
			this.value = value;
		}
	}
}

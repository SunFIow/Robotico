package net.minecraft.util.registry;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class Registry<T> {
	private static final Map<ResourceLocation, Supplier<?>> LOCATION_TO_SUPPLIER = Maps.newLinkedHashMap();
	public static final ResourceLocation ROOT = new ResourceLocation("root");
	protected static final MutableRegistry<MutableRegistry<?>> ROOT_REGISTRY = new SimpleRegistry<>(createKey("root"), Lifecycle.experimental());

	private final RegistryKey<? extends Registry<T>> registryKey;
	private final Lifecycle lifecycle;

	public static final RegistryKey<Registry<EntityType<?>>> ENTITY_TYPE_KEY = createKey("entity_type");
	public static final RegistryKey<Registry<Item>> ITEM_KEY = createKey("item");

	@Deprecated
	public static final DefaultedRegistry<EntityType<?>> ENTITY_TYPE = forge2(ENTITY_TYPE_KEY, "pig", () -> {
		return EntityType.PIG;
	});

	@Deprecated
	public static final DefaultedRegistry<Item> ITEM = forge(ITEM_KEY, "air", () -> {
		return Items.AIR;
	});

	protected Registry(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
		this.registryKey = registryKey;
		this.lifecycle = lifecycle;
	}

	public RegistryKey<? extends Registry<T>> getRegistryKey() {
		return this.registryKey;
	}

	@Override
	public String toString() {
		return "Registry[" + this.registryKey + " (" + this.lifecycle + ")]";
	}

	public static class RegistryKey<T> {
		private static final Map<String, RegistryKey<?>> UNIVERSAL_KEY_MAP = Collections.synchronizedMap(Maps.newIdentityHashMap());
		private final ResourceLocation parent;
		private final ResourceLocation location;

		public static <T> RegistryKey<T> getOrCreateKey(RegistryKey<? extends Registry<T>> parent, ResourceLocation location) {
			return getOrCreateKey(parent.location, location);
		}

		public static <T> RegistryKey<Registry<T>> getOrCreateRootKey(ResourceLocation location) {
			return getOrCreateKey(Registry.ROOT, location);
		}

		private static <T> RegistryKey<T> getOrCreateKey(ResourceLocation parent, ResourceLocation location) {
			String s = (parent + ":" + location).intern();
			return (RegistryKey<T>) UNIVERSAL_KEY_MAP.computeIfAbsent(s, (concatKey) -> {
				return new RegistryKey(parent, location);
			});
		}

		private RegistryKey(ResourceLocation parent, ResourceLocation location) {
			this.parent = parent;
			this.location = location;
		}

		@Override
		public String toString() {
			return "ResourceKey[" + this.parent + " / " + this.location + ']';
		}

		public ResourceLocation getLocation() {
			return location;
		}
	}

	public static class DefaultedRegistry<T> extends SimpleRegistry<T> {
		/** The key of the default value. */
		private final ResourceLocation defaultValueKey;
		/** The default value for this registry, retrurned in the place of a null value. */
		private T defaultValue;

		public DefaultedRegistry(String defaultValueKey, RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
			super(registryKey, lifecycle);
			this.defaultValueKey = new ResourceLocation(defaultValueKey);
		}

		public Object getKey(Item item) {
			return null;
		}

		@Override
		@Nonnull
		public T getOrDefault(@Nullable ResourceLocation name) {
			T t = super.getOrDefault(name);
			return t == null ? this.defaultValue : t;
		}

	}

	private static <T> RegistryKey<Registry<T>> createKey(String name) {
		return RegistryKey.getOrCreateRootKey(new ResourceLocation(name));
	}

	private static <T extends IForgeRegistryEntry<T>> DefaultedRegistry<T> forge(RegistryKey<? extends Registry<T>> key, String defKey, Supplier<T> def) {
		return forge(key, defKey, Lifecycle.experimental(), def);
	}

	private static <T extends IForgeRegistryEntry<T>> DefaultedRegistry<T> forge(RegistryKey<? extends Registry<T>> key, String defKey, Lifecycle cycle, Supplier<T> def) {
		return (DefaultedRegistry<T>) addRegistry(key, GameData.getWrapper(key, cycle, defKey), def, cycle);
	}

	private static <T extends IForgeRegistryEntry<T>> DefaultedRegistry<T> forge2(RegistryKey<Registry<EntityType<?>>> entityTypeKey, String defKey, Supplier<T> supplier) {
		return forge2(entityTypeKey, defKey, Lifecycle.experimental(), supplier);
	}

	private static <T extends IForgeRegistryEntry<T>> DefaultedRegistry<T> forge2(RegistryKey<Registry<EntityType<?>>> entityTypeKey, String defKey, Lifecycle cycle, Supplier<T> supplier) {
		return (DefaultedRegistry<T>) addRegistry2(entityTypeKey, GameData.getWrapper(entityTypeKey, cycle, defKey), supplier, cycle);
	}

	/**
	 * Registers the passed registry
	 */
	private static <T, R extends MutableRegistry<T>> R addRegistry(RegistryKey<? extends Registry<T>> registryKey, R instance, Supplier<T> objectSupplier, Lifecycle lifecycle) {
		ResourceLocation resourcelocation = registryKey.getLocation();
		LOCATION_TO_SUPPLIER.put(resourcelocation, objectSupplier);
		MutableRegistry<R> mutableregistry = (MutableRegistry<R>) ROOT_REGISTRY;
		return (R) mutableregistry.register((RegistryKey) registryKey, instance, lifecycle);
	}

	/**
	 * Registers the passed registry
	 */
	private static <T, R extends MutableRegistry<T>> R addRegistry2(RegistryKey<Registry<EntityType<?>>> entityTypeKey, R instance, Supplier<T> supplier, Lifecycle lifecycle) {
		ResourceLocation resourcelocation = entityTypeKey.getLocation();
		LOCATION_TO_SUPPLIER.put(resourcelocation, supplier);
		MutableRegistry<R> mutableregistry = (MutableRegistry<R>) ROOT_REGISTRY;
		return (R) mutableregistry.register((RegistryKey) entityTypeKey, instance, lifecycle);
	}

	public static <T> T register(Registry<? super T> registry, String identifier, T value) {
		return register(registry, new ResourceLocation(identifier), value);
	}

	public static <V, T extends V> T register(Registry<V> registry, ResourceLocation identifier, T value) {
		return ((MutableRegistry<V>) registry).register(RegistryKey.getOrCreateKey(registry.registryKey, identifier), value, Lifecycle.stable());
	}

	public static <T extends Entity> EntityType<T> register(DefaultedRegistry<EntityType<?>> entityType, String key, Object build) {
		return null;
	}

}

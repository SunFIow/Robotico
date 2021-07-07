package net.minecraft.entity;

import com.sunflow.world.World;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IForgeRegistryEntry;
import net.minecraft.util.registry.Registry;

public class EntityType<T extends Entity> implements IForgeRegistryEntry<EntityType<? extends Entity>> {

	public static final EntityType<PigEntity> PIG = register("pig", EntityType.Builder.<PigEntity>create(PigEntity::new, EntityClassification.CREATURE).size(0.9F, 0.9F).trackingRange(10));

	@Override
	public EntityType<? extends Entity> setRegistryName(ResourceLocation name) {
		return null;
	}

	private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, key, builder.build(key));
	}

	@Override
	public ResourceLocation getRegistryName() {
		return null;
	}

	@Override
	public Class<EntityType<? extends Entity>> getRegistryType() {
		return null;
	}

	private static enum EntityClassification {
		MONSTER("monster", 70, false, false, 128),
		CREATURE("creature", 10, true, true, 128),
		AMBIENT("ambient", 15, true, false, 128),
		WATER_CREATURE("water_creature", 5, true, false, 128),
		WATER_AMBIENT("water_ambient", 20, true, false, 64),
		MISC("misc", -1, true, true, 128);

		EntityClassification(String string, int i, boolean b, boolean c, int j) {}
	}

	private static class Builder<T extends Entity> {
		private final EntityType.IFactory<T> factory;
		private final EntityClassification classification;
		private boolean field_225436_f;

		private Builder(EntityType.IFactory<T> factoryIn, EntityClassification classificationIn) {
			this.factory = factoryIn;
			this.classification = classificationIn;
			this.field_225436_f = classificationIn == EntityClassification.CREATURE || classificationIn == EntityClassification.MISC;
		}

		public Object build(String key) {
			return null;
		}

		public Builder trackingRange(int i) {
			return this;
		}

		public Builder size(float f, float g) {
			return this;
		}

		public static <T extends Entity> EntityType.Builder<T> create(EntityType.IFactory<T> factoryIn, EntityClassification classificationIn) {
			return new EntityType.Builder<>(factoryIn, classificationIn);
		}

		public static <T extends Entity> EntityType.Builder<T> create(EntityClassification classificationIn) {
			return new EntityType.Builder<>((type, world) -> {
				return (T) null;
			}, classificationIn);
		}

	}

	public interface IFactory<T extends Entity> {
		T create(EntityType<T> p_create_1_, World p_create_2_);
	}

	public String getName() {
		return null;
	}
}
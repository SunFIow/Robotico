package com.mojang.serialization;

import java.nio.ByteBuffer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.serialization.codecs.PrimitiveCodec;

public interface Codec<C> extends Encoder<C>, Decoder<C> {
	default Codec<C> withLifecycle(final Lifecycle lifecycle) {
		return new Codec<C>() {
			@Override
			public <T> DataResult<T> encode(final C input, final DynamicOps<T> ops, final T prefix) {
				return Codec.this.encode(input, ops, prefix).setLifecycle(lifecycle);
			}

			@Override
			public <T> DataResult<Pair<C, T>> decode(final DynamicOps<T> ops, final T input) {
				return Codec.this.decode(ops, input).setLifecycle(lifecycle);
			}

			@Override
			public String toString() {
				return Codec.this.toString();
			}
		};
	}

	default Codec<C> stable() {
		return withLifecycle(Lifecycle.stable());
	}

	default Codec<C> deprecated(final int since) {
		return withLifecycle(Lifecycle.deprecated(since));
	}

	static <C> Codec<C> of(final Encoder<C> encoder, final Decoder<C> decoder) {
		return of(encoder, decoder, "Codec[" + encoder + " " + decoder + "]");
	}

	// private
	static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRange(final N minInclusive, final N maxInclusive) {
		return value -> {
			if (value.compareTo(minInclusive) >= 0 && value.compareTo(maxInclusive) <= 0) {
				return DataResult.success(value);
			}
			return DataResult.error("Value " + value + " outside of range [" + minInclusive + ":" + maxInclusive + "]", value);
		};
	}

	static <C> Codec<C> of(final Encoder<C> encoder, final Decoder<C> decoder, final String name) {
		return new Codec<C>() {
			@Override
			public <T> DataResult<Pair<C, T>> decode(final DynamicOps<T> ops, final T input) {
				return decoder.decode(ops, input);
			}

			@Override
			public <T> DataResult<T> encode(final C input, final DynamicOps<T> ops, final T prefix) {
				return encoder.encode(input, ops, prefix);
			}

			@Override
			public String toString() {
				return name;
			}
		};
	}

	default <S> Codec<S> comapFlatMap(final Function<? super C, ? extends DataResult<? extends S>> to, final Function<? super S, ? extends C> from) {
		return Codec.of(comap(from), flatMap(to), toString() + "[comapFlatMapped]");
	}

	PrimitiveCodec<Byte> BYTE = new PrimitiveCodec<Byte>() {
		@Override
		public <T> DataResult<Byte> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getNumberValue(input)
					.map(Number::byteValue);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final Byte value) {
			return ops.createByte(value);
		}

		@Override
		public String toString() {
			return "Byte";
		}
	};

	PrimitiveCodec<Short> SHORT = new PrimitiveCodec<Short>() {
		@Override
		public <T> DataResult<Short> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getNumberValue(input)
					.map(Number::shortValue);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final Short value) {
			return ops.createShort(value);
		}

		@Override
		public String toString() {
			return "Short";
		}
	};

	PrimitiveCodec<Integer> INT = new PrimitiveCodec<Integer>() {
		@Override
		public <T> DataResult<Integer> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getNumberValue(input)
					.map(Number::intValue);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final Integer value) {
			return ops.createInt(value);
		}

		@Override
		public String toString() {
			return "Int";
		}
	};

	PrimitiveCodec<Long> LONG = new PrimitiveCodec<Long>() {
		@Override
		public <T> DataResult<Long> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getNumberValue(input)
					.map(Number::longValue);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final Long value) {
			return ops.createLong(value);
		}

		@Override
		public String toString() {
			return "Long";
		}
	};

	PrimitiveCodec<Float> FLOAT = new PrimitiveCodec<Float>() {
		@Override
		public <T> DataResult<Float> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getNumberValue(input)
					.map(Number::floatValue);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final Float value) {
			return ops.createFloat(value);
		}

		@Override
		public String toString() {
			return "Float";
		}
	};

	PrimitiveCodec<Double> DOUBLE = new PrimitiveCodec<Double>() {
		@Override
		public <T> DataResult<Double> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getNumberValue(input)
					.map(Number::doubleValue);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final Double value) {
			return ops.createDouble(value);
		}

		@Override
		public String toString() {
			return "Double";
		}
	};

	PrimitiveCodec<String> STRING = new PrimitiveCodec<String>() {
		@Override
		public <T> DataResult<String> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getStringValue(input);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final String value) {
			return ops.createString(value);
		}

		@Override
		public String toString() {
			return "String";
		}
	};

	PrimitiveCodec<ByteBuffer> BYTE_BUFFER = new PrimitiveCodec<ByteBuffer>() {
		@Override
		public <T> DataResult<ByteBuffer> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getByteBuffer(input);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final ByteBuffer value) {
			return ops.createByteList(value);
		}

		@Override
		public String toString() {
			return "ByteBuffer";
		}
	};

	PrimitiveCodec<IntStream> INT_STREAM = new PrimitiveCodec<IntStream>() {
		@Override
		public <T> DataResult<IntStream> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getIntStream(input);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final IntStream value) {
			return ops.createIntList(value);
		}

		@Override
		public String toString() {
			return "IntStream";
		}
	};

	PrimitiveCodec<LongStream> LONG_STREAM = new PrimitiveCodec<LongStream>() {
		@Override
		public <T> DataResult<LongStream> read(final DynamicOps<T> ops, final T input) {
			return ops
					.getLongStream(input);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final LongStream value) {
			return ops.createLongList(value);
		}

		@Override
		public String toString() {
			return "LongStream";
		}
	};

	Codec<Dynamic<?>> PASSTHROUGH = new Codec<Dynamic<?>>() {
		@Override
		public <T> DataResult<Pair<Dynamic<?>, T>> decode(final DynamicOps<T> ops, final T input) {
			return DataResult.success(Pair.of(new Dynamic<>(ops, input), ops.empty()));
		}

		@Override
		public <T> DataResult<T> encode(final Dynamic<?> input, final DynamicOps<T> ops, final T prefix) {
			if (input.getValue() == input.getOps().empty()) {
				// nothing to merge, return rest
				return DataResult.success(prefix, Lifecycle.experimental());
			}

			final T casted = input.convert(ops).getValue();
			if (prefix == ops.empty()) {
				// no need to merge anything, return the old value
				return DataResult.success(casted, Lifecycle.experimental());
			}

			final DataResult<T> toMap = ops.getMap(casted).flatMap(map -> ops.mergeToMap(prefix, map));
			return toMap.result().map(DataResult::success).orElseGet(() -> {
				final DataResult<T> toList = ops.getStream(casted).flatMap(stream -> ops.mergeToList(prefix, stream.collect(Collectors.toList())));
				return toList.result().map(DataResult::success).orElseGet(() -> DataResult.error("Don't know how to merge " + prefix + " and " + casted, prefix, Lifecycle.experimental()));
			});
		}

		@Override
		public String toString() {
			return "passthrough";
		}
	};

}

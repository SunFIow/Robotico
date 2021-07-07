package com.mojang.serialization;

import java.util.function.Function;

public interface Encoder<C> {
	<T> DataResult<T> encode(final C input, final DynamicOps<T> ops, final T prefix);

	default <T> DataResult<T> encodeStart(final DynamicOps<T> ops, final C input) {
		return encode(input, ops, ops.empty());
	}

	default <B> Encoder<B> comap(final Function<? super B, ? extends C> function) {
		return new Encoder<B>() {
			@Override
			public <T> DataResult<T> encode(final B input, final DynamicOps<T> ops, final T prefix) {
				return Encoder.this.encode(function.apply(input), ops, prefix);
			}

			@Override
			public String toString() {
				return Encoder.this.toString() + "[comapped]";
			}
		};
	}
}

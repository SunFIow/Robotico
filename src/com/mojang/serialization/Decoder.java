package com.mojang.serialization;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

public interface Decoder<C> {
	<T> DataResult<Pair<C, T>> decode(final DynamicOps<T> ops, final T input);

	// TODO: rename to read after Type.read is no more
	default <T> DataResult<C> parse(final DynamicOps<T> ops, final T input) {
		return decode(ops, input).map(Pair::getLeft);
	}

	default <B> Decoder<B> flatMap(final Function<? super C, ? extends DataResult<? extends B>> function) {
		return new Decoder<B>() {
			@Override
			public <T> DataResult<Pair<B, T>> decode(final DynamicOps<T> ops, final T input) {
				return Decoder.this.decode(ops, input).flatMap(p -> function.apply(p.getLeft()).map(r -> Pair.of(r, p.getRight())));
			}

			@Override
			public String toString() {
				return Decoder.this.toString() + "[flatMapped]";
			}
		};
	}

}

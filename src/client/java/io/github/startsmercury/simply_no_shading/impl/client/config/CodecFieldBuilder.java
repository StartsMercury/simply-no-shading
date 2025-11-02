package io.github.startsmercury.simply_no_shading.impl.client.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;

@FunctionalInterface
public interface CodecFieldBuilder {
    CodecFieldBuilder DEFAULT = CodecFieldBuilder::fieldOfImpl;
    CodecFieldBuilder OPTIONAL = Codec::optionalFieldOf;

    private static <A> MapCodec<A> fieldOfImpl(
        final Codec<A> self,
        final String name,
        final A defaultValue
    ) {
        return self.fieldOf(name);
    }

    <A> MapCodec<A> create(Codec<A> self, String name, A defaultValue);

    default <O, A> RecordCodecBuilder<O, A> create(
        final Codec<A> self,
        final String name,
        final A defaultValue,
        final Function<O, A> getter
    ) {
        return create(self, name, defaultValue).forGetter(getter);
    }
}

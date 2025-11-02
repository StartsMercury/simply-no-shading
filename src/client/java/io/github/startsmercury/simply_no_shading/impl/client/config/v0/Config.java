package io.github.startsmercury.simply_no_shading.impl.client.config.v0;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.startsmercury.simply_no_shading.impl.client.config.CodecFieldBuilder;
import io.github.startsmercury.simply_no_shading.impl.client.config.IConfig;

/**
 * The Simply No Shading modification configuration, format version {@code 0}.
 *
 * @param blockShadingEnabled  Should non-entity blocks be shaded.
 * @param cloudShadingEnabled  Should clouds be shaded.
 * @param entityShadingEnabled  Should entities and block entities be shaded.
 * @since 8.0.0
 */
public record Config(
    boolean blockShadingEnabled,
    boolean cloudShadingEnabled,
    boolean entityShadingEnabled
) implements IConfig {
    /**
     * The configuration format version.
     *
     * @implSpec The version must be {@code 0}.
     */
    public static final int VERSION = 0;

    /**
     * @implSpec The version must be {@code 0}.
     */
    @Override
    public int version() {
        return Config.VERSION;
    }

    private static MapCodec<Config> newCodec(final CodecFieldBuilder fieldBuilder) {
        return RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                fieldBuilder.create(
                    Codec.BOOL,
                    "blockShadingEnabled",
                    false,
                    Config::blockShadingEnabled
                ),
                fieldBuilder.create(
                    Codec.BOOL,
                    "cloudShadingEnabled",
                    false,
                    Config::cloudShadingEnabled
                ),
                fieldBuilder.create(
                    Codec.BOOL,
                    "entityShadingEnabled",
                    false,
                    Config::entityShadingEnabled
                )
            ).apply(instance, Config::new));
    }

    public static final MapCodec<Config> LENIENT_CODEC = newCodec(CodecFieldBuilder.OPTIONAL);

    public static final MapCodec<Config> CODEC = newCodec(CodecFieldBuilder.DEFAULT);
}

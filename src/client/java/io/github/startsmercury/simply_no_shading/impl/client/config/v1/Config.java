package io.github.startsmercury.simply_no_shading.impl.client.config.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.startsmercury.simply_no_shading.impl.client.config.CodecFieldBuilder;
import io.github.startsmercury.simply_no_shading.impl.client.config.IConfig;
import java.util.Optional;

/**
 * The Simply No Shading modification configuration, format version {@code 1}.
 *
 * @param compatibilityMode  The compatibility mode hint. This may have more
 *     effect when disabling shading. The effects may vary.
 * @param preset  The configuration preset.
 * @param custom  The persistent configuration data for the custom preset.
 * @since 8.0.0
 */
public record Config(
    boolean compatibilityMode,
    ConfigPreset preset,
    Optional<ConfigData> custom
) implements IConfig {
    /**
     * The configuration format version.
     *
     * @implSpec The version must be {@code 1}.
     */
    public static final int VERSION = 1;

    /**
     * Creates a config builder.
     *
     * @return a config builder.
     */
    public static ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    /**
     * @implSpec The version must be {@code 1}.
     */
    @Override
    public int version() {
        return Config.VERSION;
    }

    /**
     * The default configuration.
     *
     * @implSpec The default config notably uses the {@code SIMPLY_NO_SHADING}
     *     preset with an {@code empty} custom data and others such as pre-enabled
     *     compatibility mode.
     */
    public static final Config DEFAULT = new Config(
        true,
        ConfigPreset.SIMPLY_NO_SHADING,
        Optional.empty()
    );

    /**
     * Most of the configuration data.
     * <p>
     * This is a convenience method in accessing the usable config data. To check
     * the retained customized data, see {@code custom}.
     *
     * @see #custom
     */
    public ConfigData data() {
        return this.preset.override().or(this::custom).orElse(ConfigData.DEFAULT);
    }

    private static MapCodec<Config> newCodec(final CodecFieldBuilder fieldBuilder) {
        return RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                fieldBuilder.create(
                    Codec.BOOL,
                    "compatibilityMode",
                    false,
                    Config::compatibilityMode
                ),
                fieldBuilder.create(
                    ConfigPreset.CODEC,
                    "preset",
                    ConfigPreset.CUSTOM,
                    Config::preset
                ),
                ConfigData.CODEC.optionalFieldOf("custom").forGetter(Config::custom)
            ).apply(instance, Config::new));
    }

    public static final MapCodec<Config> LENIENT_CODEC = newCodec(CodecFieldBuilder.OPTIONAL);

    public static final MapCodec<Config> CODEC = newCodec(CodecFieldBuilder.DEFAULT);
}

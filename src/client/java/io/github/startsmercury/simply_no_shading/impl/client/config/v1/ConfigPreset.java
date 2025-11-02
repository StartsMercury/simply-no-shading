package io.github.startsmercury.simply_no_shading.impl.client.config.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * The configuration preset.
 *
 * @since 8.0.0
 */
public enum ConfigPreset {
    /** All shading types are enabled on vanilla. */
    VANILLA(ConfigData.VANILLA),
    /** Old block lighting from internal shader. */
    INTERNAL_SHADERS(ConfigData.INTERNAL_SHADERS),
    /** Simply No Shading defaults disables all shading types. */
    SIMPLY_NO_SHADING(ConfigData.DEFAULT),
    /** The customized preset is internally retained across presets. */
    CUSTOM,
    ;

    private static final List<ConfigPreset> VALUE_LIST = List.of(values());

    public static List<ConfigPreset> valueList() {
        return VALUE_LIST;
    }

    public static final Codec<ConfigPreset> CODEC = Codec.STRING.comapFlatMap(
        input -> {
            try {
                return DataResult.success(valueOf(input.toUpperCase(Locale.ROOT)));
            } catch (final IllegalArgumentException cause) {
                return DataResult.error(() -> "Unrecognized configuration preset");
            }
        },
        self -> self.name().toLowerCase(Locale.ROOT)
    );

    /**
     * Preset override allow preset switching without losing the custom config.
     */
    private final Optional<ConfigData> override;

    ConfigPreset() {
        this.override = Optional.empty();
    }

    ConfigPreset(final ConfigData override) {
        this.override = Optional.of(override);
    }

    /**
     * Gets the config override for this preset.
     * <p>
     * Preset overrides allow preset switching without losing the custom config.
     *
     * @return The config override for this preset.
     * @implSpec <pre>{@code
     *     switch (this) {
     *         VANILLA -> Optional.of(ConfigData.VANILLA);
     *         INTERNAL_SHADERS -> Optional.of(ConfigData.INTERNAL_SHADERS);
     *         SIMPLY_NO_SHADING -> Optional.of(ConfigData.SIMPLY_NO_SHADING);
     *         CUSTOM -> Option.empty();
     *     }
     * }</pre>
     */
    public Optional<ConfigData> override() {
        return this.override;
    }
}

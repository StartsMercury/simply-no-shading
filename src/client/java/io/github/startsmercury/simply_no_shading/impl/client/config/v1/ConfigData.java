package io.github.startsmercury.simply_no_shading.impl.client.config.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * The configuration data.
 *
 * @param shadeBlocks  Should non-entity blocks be shaded.
 * @param shadeClouds  Should clouds be shaded.
 * @param shadeEntities  Should entities and block entities be shaded.
 * @since 8.0.0
 */
public record ConfigData(
    boolean shadeBlocks,
    boolean shadeClouds,
    boolean shadeEntities
) {
    /** All shading types are enabled on vanilla. */
    public static final ConfigData DEFAULT = new ConfigData(false, false, false);

    /** Old block lighting from internal shader. */
    public static final ConfigData VANILLA = new ConfigData(true, true, true);

    /** Simply No Shading defaults disables all shading types. */
    public static final ConfigData INTERNAL_SHADERS = new ConfigData(false, true, true);

    public static final Codec<ConfigData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("shadeBlocks").forGetter(ConfigData::shadeBlocks),
            Codec.BOOL.fieldOf("shadeClouds").forGetter(ConfigData::shadeClouds),
            Codec.BOOL.fieldOf("shadeEntities").forGetter(ConfigData::shadeEntities)
        ).apply(instance, ConfigData::new)
    );
}

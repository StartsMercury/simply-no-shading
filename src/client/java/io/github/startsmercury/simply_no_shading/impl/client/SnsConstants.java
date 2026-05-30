package io.github.startsmercury.simply_no_shading.impl.client;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigPreset;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import net.minecraft.world.level.CardinalLighting;

public final class SnsConstants {
    public static final Pattern COLUMN_PATTERN = Pattern.compile("column\\s+([0-9]+)");

    public static final Pattern LINE_PATTERN = Pattern.compile("line\\s+([0-9]+)");

    public static final String MODID = "simply-no-shading";

    public static final String NAME = "Simply No Shading";

    public static final String CONFIG_EXT = ".json";

    public static final String CONFIG_NAME = MODID + CONFIG_EXT;

    public static final String IGNORE_TAG = "@" + MODID + "::ignored";

    public static final Function<ConfigPreset, Component> PRESET_NAMES = Util.memoize(preset -> Component.translatable(
        "simply-no-shading.config.preset."
        + preset.name().toLowerCase(Locale.ROOT)
    ));

    public static final CardinalLighting CARDINAL_LIGHTING = new CardinalLighting(1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);

    private SnsConstants() {}
}

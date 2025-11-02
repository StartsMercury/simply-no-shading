package io.github.startsmercury.simply_no_shading.impl.client;

import java.util.regex.Pattern;

public final class SnsConstants {
    public static final Pattern COLUMN_PATTERN = Pattern.compile("column\\s+([0-9]+)");

    public static final Pattern LINE_PATTERN = Pattern.compile("line\\s+([0-9]+)");

    public static final String MODID = "simply-no-shading";

    public static final String NAME = "Simply No Shading";

    public static final String CONFIG_EXT = ".json";

    public static final String CONFIG_NAME = MODID + CONFIG_EXT;

    public static final String IGNORE_TAG = "@" + MODID + "::ignored";

    private SnsConstants() {}
}

package io.github.startsmercury.simply_no_shading.impl.client;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

import io.github.startsmercury.simply_no_shading.api.client.Config;
import java.util.List;

public enum ShadingTarget {
    BLOCK(),
    CLOUD(),
    ENTITY();

    private static final List<ShadingTarget> VALUE_LIST = List.of(values());

    public static List<? extends ShadingTarget> valueList() {
        return VALUE_LIST;
    }

    private final String toggleKey;

    private final String toString;

    ShadingTarget() {
        this.toString = UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).convert(this.name());
        this.toggleKey = "toggle" + UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).convert(this.name()) + "Shading";
    }

    public boolean getFrom(final Config config) {
        return switch (this) {
            case BLOCK -> config.blockShadingEnabled();
            case CLOUD -> config.cloudShadingEnabled();
            case ENTITY -> config.entityShadingEnabled();
        };
    }

    public boolean changedBetween(final Config lhs, final Config rhs) {
        return this.getFrom(lhs) != this.getFrom(rhs);
    }

    public void setInto(final Config config, final boolean enabled) {
        System.err.println(this + ": " + this.getFrom(config) + " -> " + enabled);
        switch (this) {
            case BLOCK -> config.setBlockShadingEnabled(enabled);
            case CLOUD -> config.setCloudShadingEnabled(enabled);
            case ENTITY -> config.setEntityShadingEnabled(enabled);
        }
    }

    public ReloadLevel reloadTypeFor(final GameContext context) {
        return switch (this) {
            case BLOCK -> {
                if (context.shadersEnabled()) {
                    yield ReloadLevel.NONE;
                } else {
                    yield ReloadLevel.ALL_CHANGED;
                }
            }
            case CLOUD -> {
                if (context.shadersEnabled()) {
                    yield ReloadLevel.NONE;
                } else if (context.sodiumLoaded()) {
                    yield ReloadLevel.ALL_CHANGED;
                } else {
                    yield ReloadLevel.NEEDS_UPDATE;
                }
            }
            case ENTITY -> ReloadLevel.RESOURCE_PACKS;
        };
    }

    public String toggleKey() {
        return this.toggleKey;
    }

    @Override
    public String toString() {
        return this.toString;
    }
}

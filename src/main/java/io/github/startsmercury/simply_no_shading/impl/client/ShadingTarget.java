package io.github.startsmercury.simply_no_shading.impl.client;

import static com.google.common.base.CaseFormat.*;

import io.github.startsmercury.simply_no_shading.api.client.Config;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ShadingTarget {
    BLOCK() {
        @Override
        public boolean getFrom(Config config) {
            return config.blockShadingEnabled();
        }

        @Override
        public ReloadLevel reloadTypeFor(GameContext context) {
            if (context.shadersEnabled()) {
                return ReloadLevel.NONE;
            } else {
                return ReloadLevel.ALL_CHANGED;
            }
        }
    },
    CLOUD() {
        @Override
        public boolean getFrom(Config config) {
            return config.cloudShadingEnabled();
        }

        @Override
        public ReloadLevel reloadTypeFor(GameContext context) {
            if (context.shadersEnabled()) {
                return ReloadLevel.NONE;
            } else if (context.sodiumLoaded()) {
                return ReloadLevel.ALL_CHANGED;
            } else {
                return ReloadLevel.NEEDS_UPDATE;
            }
        }
    },
    ENTITY() {
        @Override
        public boolean getFrom(Config config) {
            return config.entityShadingEnabled();
        }

        @Override
        public ReloadLevel reloadTypeFor(GameContext context) {
            return ReloadLevel.NONE;
        }
    };

    private static final List<ShadingTarget> VALUE_LIST =
        Collections.unmodifiableList(Arrays.asList(values()));

    public static List<? extends ShadingTarget> valueList() {
        return VALUE_LIST;
    }

    private final String toggleKey;

    private final String toString;

    ShadingTarget() {
        this.toString = UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).convert(this.name());
        this.toggleKey = "toggle_"
            + UPPER_UNDERSCORE.converterTo(LOWER_UNDERSCORE).convert(this.name())
            + "_shading";
    }

    public abstract boolean getFrom(final Config config);

    public boolean changedBetween(final Config lhs, final Config rhs) {
        return this.getFrom(lhs) != this.getFrom(rhs);
    }

    public void setInto(final Config config, final boolean enabled) {
        switch (this) {
            case BLOCK: config.setBlockShadingEnabled(enabled);
            case CLOUD: config.setCloudShadingEnabled(enabled);
            case ENTITY: config.setEntityShadingEnabled(enabled);
        }
    }

    public abstract ReloadLevel reloadTypeFor(final GameContext context);

    public String toggleKey() {
        return this.toggleKey;
    }

    @Override
    public String toString() {
        return this.toString;
    }
}

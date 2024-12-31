package io.github.startsmercury.simply_no_shading.impl.client;

import static com.google.common.base.CaseFormat.*;

import io.github.startsmercury.simply_no_shading.api.client.Config;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ShadingTarget {
    BLOCK() {
        @Override
        public boolean getFrom(final Config config) {
            return config.blockShadingEnabled();
        }

        @Override
        public void setInto(final Config config, final boolean enabled) {
            config.setBlockShadingEnabled(enabled);
        }

        @Override
        public ReloadLevel reloadTypeFor(final GameContext context) {
            return ReloadLevel.ALL_CHANGED;
        }
    },
    CLOUD() {
        @Override
        public boolean getFrom(final Config config) {
            return config.cloudShadingEnabled();
        }

        @Override
        public void setInto(final Config config, final boolean enabled) {
            config.setCloudShadingEnabled(enabled);
        }

        @Override
        public ReloadLevel reloadTypeFor(final GameContext context) {
            if (context.sodiumLoaded()) {
                return ReloadLevel.ALL_CHANGED;
            } else {
                return ReloadLevel.NEEDS_UPDATE;
            }
        }
    },
    ENTITY() {
        @Override
        public boolean getFrom(final Config config) {
            return config.entityShadingEnabled();
        }

        @Override
        public void setInto(final Config config, final boolean enabled) {
            config.setEntityShadingEnabled(enabled);
        }

        @Override
        public ReloadLevel reloadTypeFor(final GameContext context) {
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
        this.toggleKey = "toggle" + UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).convert(this.name()) + "Shading";
    }

    public abstract boolean getFrom(Config config);

    public boolean changedBetween(final Config lhs, final Config rhs) {
        return this.getFrom(lhs) != this.getFrom(rhs);
    }

    public abstract void setInto(Config config, boolean enabled);

    public abstract ReloadLevel reloadTypeFor(final GameContext context);

    public String toggleKey() {
        return this.toggleKey;
    }

    @Override
    public String toString() {
        return this.toString;
    }
}

package io.github.startsmercury.simply_no_shading.impl.client;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

public enum ShadingTarget {
    BLOCK(ReloadType.blocks()),
    CLOUD(ReloadType.clouds()),
    ENTITY(ReloadType.entities());

    private static final List<ShadingTarget> VALUE_LIST = List.of(values());

    public static List<? extends ShadingTarget> valueList() {
        return VALUE_LIST;
    }

    private final String configKey;

    private final ReloadType reloadType;

    private final String toggleKey;

    private final String toString;

    ShadingTarget(final ReloadType reloadType) {
        this.toString = UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).convert(this.name());
        this.configKey = toString + "ShadingEnabled";
        this.reloadType = reloadType;
        this.toggleKey = "toggle" + UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).convert(this.name()) + "Shading";
    }

    public ReloadType reloadType() {
        return this.reloadType;
    }

    public String toggleKey() {
        return this.toggleKey;
    }

    @Override
    public String toString() {
        return this.toString;
    }

    public Boolean fromJson(final JsonObject object) {
        return object.get(this.configKey) instanceof final JsonPrimitive leaf && leaf.isBoolean() ? leaf.getAsBoolean() : null;
    }

    public void intoJson(final JsonObject object, final boolean value) {
        object.addProperty(this.configKey, value);
    }
}

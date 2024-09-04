package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ConfigImpl implements Config {
    boolean @NotNull [] shadingValues;

    public ConfigImpl() {
        this.shadingValues = new boolean[ShadingTarget.valueList().size()];
    }

    public ConfigImpl(final Config other) {
        this();
        this.set(other);
    }

    public boolean shadingEnabled(final ShadingTarget target) {
        return this.shadingValues[target.ordinal()];
    }

    public void setShadingEnabled(final ShadingTarget target, final boolean enabled) {
        this.shadingValues[target.ordinal()] = enabled;
    }

    @Override
    public boolean blockShadingEnabled() {
        return shadingEnabled(ShadingTarget.BLOCK);
    }

    @Override
    public void setBlockShadingEnabled(final boolean enabled) {
        setShadingEnabled(ShadingTarget.BLOCK, enabled);
    }

    @Override
    public boolean cloudShadingEnabled() {
        return shadingEnabled(ShadingTarget.CLOUD);
    }

    @Override
    public void setCloudShadingEnabled(final boolean enabled) {
        setShadingEnabled(ShadingTarget.CLOUD, enabled);
    }

    @Override
    public boolean entityShadingEnabled() {
        return shadingEnabled(ShadingTarget.ENTITY);
    }

    @Override
    public void setEntityShadingEnabled(final boolean enabled) {
        setShadingEnabled(ShadingTarget.ENTITY, enabled);
    }

    public void set(final Config other) {
        Objects.requireNonNull(other);
        this.setBlockShadingEnabled(other.blockShadingEnabled());
        this.setCloudShadingEnabled(other.cloudShadingEnabled());
        if (other instanceof final ConfigImpl otherImpl) this.setEntityShadingEnabled(otherImpl.entityShadingEnabled());
    }

    public void fromJson(final JsonElement json) {
        if (!(json instanceof final JsonObject jsonObject)) {
            throw new JsonParseException(
                "Cannot parse "
                    + this.getClass().getName()
                    + " from "
                    + json.getClass().getName()
            );
        }

        for (final var target : ShadingTarget.valueList()) {
            final var enabled = target.fromJson(jsonObject);
            if (enabled != null) {
                setShadingEnabled(target, enabled);
            }
        }
    }

    public void toJson(final JsonObject tree) {
        for (final var target : ShadingTarget.valueList()) {
            target.intoJson(tree, shadingEnabled(target));
        }
    }
}

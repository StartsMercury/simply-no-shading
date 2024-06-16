package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;

public class ConfigImpl implements Config {
    final Reference2BooleanMap<String> shadingValues;

    public ConfigImpl(final boolean blockShadingEnabled, final boolean cloudShadingEnabled) {
        this.shadingValues = new Reference2BooleanOpenHashMap<>();
        this.set(blockShadingEnabled, cloudShadingEnabled);
        this.setShaderShadingEnabled(false);
    }

    public ConfigImpl(final Config other) {
        this(other.blockShadingEnabled(), other.cloudShadingEnabled());
        if (other instanceof ConfigImpl) this.setShaderShadingEnabled(((ConfigImpl) other).shaderShadingEnabled());
    }

    @Override
    public boolean blockShadingEnabled() {
        return this.shadingValues.getBoolean("blockShading");
    }

    @Override
    public void setBlockShadingEnabled(final boolean enabled) {
        this.shadingValues.put("blockShading", enabled);
    }

    @Override
    public boolean cloudShadingEnabled() {
        return this.shadingValues.getBoolean("cloudShading");
    }

    @Override
    public void setCloudShadingEnabled(final boolean enabled) {
        this.shadingValues.put("cloudShading", enabled);
    }

    public boolean shaderShadingEnabled() {
        return this.shadingValues.getBoolean("shaderShading");
    }

    public void setShaderShadingEnabled(final boolean enabled) {
        this.shadingValues.put("shaderShading", enabled);
    }

    public void set(final boolean blockShadingEnabled, final boolean cloudShadingEnabled) {
        this.setBlockShadingEnabled(blockShadingEnabled);
        this.setCloudShadingEnabled(cloudShadingEnabled);
    }

    public void set(final Config other) {
        this.set(other.blockShadingEnabled(), other.cloudShadingEnabled());
        if (other instanceof ConfigImpl) this.setShaderShadingEnabled(((ConfigImpl) other).shaderShadingEnabled());
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

        jsonObject.asMap().forEach((key, node) -> {
            if (!key.endsWith("Enabled")) {
                return;
            }
            final var shadingKey = key.substring(0, key.length() - "Enabled".length());
            if (this.shadingValues.containsKey(shadingKey) && node instanceof final JsonPrimitive enabled && enabled.isBoolean()) {
                this.shadingValues.put(shadingKey, node.getAsBoolean());
            }
        });
    }

    public void toJson(final JsonObject tree) {
        this.shadingValues.forEach(
            (property, value) -> tree.addProperty(property + "Enabled", value)
        );
    }
}

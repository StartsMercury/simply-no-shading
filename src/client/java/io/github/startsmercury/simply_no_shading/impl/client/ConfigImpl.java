package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;

public class ConfigImpl implements Config {
    final Reference2BooleanMap<String> shadingValues;

    public ConfigImpl(final boolean blockShadingEnabled, final boolean cloudShadingEnabled) {
        this.shadingValues = new Reference2BooleanOpenHashMap();
        this.set(blockShadingEnabled, cloudShadingEnabled);
    }

    public ConfigImpl(final Config other) {
        this(other.blockShadingEnabled(), other.cloudShadingEnabled());
    }

    @Override
    public boolean blockShadingEnabled() {
        return this.shadingValues.get("blockShading");
    }

    @Override
    public void setBlockShadingEnabled(final boolean enabled) {
        this.shadingValues.put("blockShading", enabled);
    }

    @Override
    public boolean cloudShadingEnabled() {
        return this.shadingValues.get("cloudShading");
    }

    @Override
    public void setCloudShadingEnabled(final boolean enabled) {
        this.shadingValues.put("cloudShading", enabled);
    }

    public void set(final boolean blockShadingEnabled, final boolean cloudShadingEnabled) {
        this.setBlockShadingEnabled(blockShadingEnabled);
        this.setCloudShadingEnabled(cloudShadingEnabled);
    }

    public void set(final Config other) {
        this.set(other.blockShadingEnabled(), other.cloudShadingEnabled());
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

        if (jsonObject.get("blockShadingEnabled") instanceof final JsonPrimitive blockShadingEnabled
            && blockShadingEnabled.isBoolean()
        ) {
            this.setBlockShadingEnabled(blockShadingEnabled.getAsBoolean());
        }

        if (jsonObject.get("cloudShadingEnabled") instanceof final JsonPrimitive cloudShadingEnabled
            && cloudShadingEnabled.isBoolean()
        ) {
            this.setCloudShadingEnabled(cloudShadingEnabled.getAsBoolean());
        }
    }

    public void toJson(final JsonObject tree) {
        tree.addProperty("blockShadingEnabled", this.blockShadingEnabled());
        tree.addProperty("cloudShadingEnabled", this.cloudShadingEnabled());
    }
}

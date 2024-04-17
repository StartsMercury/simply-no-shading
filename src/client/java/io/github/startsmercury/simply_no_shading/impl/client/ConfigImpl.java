package io.github.startsmercury.simply_no_shading.impl.client;

public class ConfigImpl implements Config {
    private boolean blockShadingEnabled;
    private boolean cloudShadingEnabled;

    public ConfigImpl(final Config other) {
        this(other.blockShadingEnabled(), other.cloudShadingEnabled());
    }

    public ConfigImpl(final boolean blockShadingEnabled, final boolean cloudShadingEnabled) {
        this.blockShadingEnabled = blockShadingEnabled;
        this.cloudShadingEnabled = cloudShadingEnabled;
    }

    @Override
    public boolean blockShadingEnabled() {
        return this.blockShadingEnabled;
    }

    @Override
    public void setBlockShadingEnabled(final boolean enabled) {
        this.blockShadingEnabled = enabled;
    }

    @Override
    public boolean cloudShadingEnabled() {
        return this.cloudShadingEnabled;
    }

    @Override
    public void setCloudShadingEnabled(final boolean enabled) {
        this.cloudShadingEnabled = enabled;
    }

    public void set(final Config other) {
        this.blockShadingEnabled = other.blockShadingEnabled;
        this.cloudShadingEnabled = other.cloudShadingEnabled;
    }

    public void fromJson(final JsonElement json) {
        if (!json instanceof final JsonObject jsonObject) {
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
            && blockShadingEnabled.isBoolean()
        ) {
            this.setCloudShadingEnabled(cloudShadingEnabled.getAsBoolean());
        }
    }

    public void toJson(final JsonObject tree) {
        tree.addProperty("blockShadingEnabled", this.blockShadingEnabled());
        tree.addProperty("cloudShadingEnabled", this.cloudShadingEnabled());

        return tree;
    }
}

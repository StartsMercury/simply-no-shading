package io.github.startsmercury.simply_no_shading.impl.client;

import io.github.startsmercury.simply_no_shading.api.client.Config;

public final class ConfigImpl implements Config {
    private boolean blockShadingEnabled;
    private boolean cloudShadingEnabled;
    private boolean entityShadingEnabled;

    public ConfigImpl() {}

    public ConfigImpl(final Config other) {
        this.setInternal(other);
    }

    public void set(final Config other) {
        this.setInternal(other);
    }

    private void setInternal(final Config other) {
        this.blockShadingEnabled = other.blockShadingEnabled();
        this.cloudShadingEnabled = other.cloudShadingEnabled();
        this.entityShadingEnabled = other.entityShadingEnabled();
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

    @Override
    public boolean entityShadingEnabled() {
        return this.entityShadingEnabled;
    }

    @Override
    public void setEntityShadingEnabled(final boolean enabled) {
        this.entityShadingEnabled = enabled;
    }
}

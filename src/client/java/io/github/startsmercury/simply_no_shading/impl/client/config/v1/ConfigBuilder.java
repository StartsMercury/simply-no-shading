package io.github.startsmercury.simply_no_shading.impl.client.config.v1;

import java.util.Optional;

public class ConfigBuilder {
    public Config build() {
        return new Config(
            this.compatibilityMode,
            this.preset,
            this.getCustom()
        );
    }

    public ConfigBuilder() {
        this(Config.DEFAULT);
    }

    public ConfigBuilder(final Config config) {
        this.setConfig(config);
    }

    public ConfigBuilder setConfig(final Config config) {
        this.compatibilityMode = config.compatibilityMode();
        this.preset = config.preset();
        this.custom = config.custom().map(Data::new);
        return this;
    }

    private boolean compatibilityMode;

    public boolean isCompatibilityMode() {
        return this.compatibilityMode;
    }

    public ConfigBuilder setCompatibilityMode(final boolean compatibilityMode) {
        this.compatibilityMode = compatibilityMode;
        return this;
    }

    private ConfigPreset preset;

    public ConfigPreset getPreset() {
        return this.preset;
    }

    public ConfigBuilder setPreset(final ConfigPreset preset) {
        this.preset = preset;
        return this;
    }

    private static class Data {
        private boolean shadeBlocks;
        private boolean shadeClouds;
        private boolean shadeEntities;

        public Data(final ConfigData data) {
            this.shadeBlocks = data.shadeBlocks();
            this.shadeClouds = data.shadeClouds();
            this.shadeEntities = data.shadeEntities();
        }

        public ConfigData build() {
            return new ConfigData(this.shadeBlocks, this.shadeClouds, this.shadeEntities);
        }
    }

    private Optional<Data> custom;

    public Optional<ConfigData> getCustom() {
        return this.custom.map(Data::build);
    }

    private Data getOrCreateCustom() {
        return this.custom.orElseGet(() -> {
            final var custom = new Data(this.preset.override().orElse(ConfigData.DEFAULT));
            this.custom = Optional.of(custom);
            return custom;
        });
    }

    public ConfigBuilder setCustom(final Optional<ConfigData> custom) {
        this.custom = custom.map(Data::new);
        return this;
    }

    public ConfigBuilder setCustom(final ConfigData custom) {
        this.custom = Optional.of(new Data(custom));
        return this;
    }

    public ConfigBuilder unsetCustom() {
        this.custom = Optional.empty();
        return this;
    }

    public ConfigBuilder setShadeBlocks(final boolean shadeBlocks) {
        this.getOrCreateCustom().shadeBlocks = shadeBlocks;
        return this;
    }

    public ConfigBuilder setShadeClouds(final boolean shadeClouds) {
        this.getOrCreateCustom().shadeClouds = shadeClouds;
        return this;
    }

    public ConfigBuilder setShadeEntities(final boolean shadeEntities) {
        this.getOrCreateCustom().shadeEntities = shadeEntities;
        return this;
    }
}

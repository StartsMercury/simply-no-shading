package io.github.startsmercury.simply_no_shading.impl.client;

public record ShadingToggle(
    String name,
    boolean defaultValue,
    KeyMapping keyMapping,
    ReloadType reloadType
) {
    public boolean getFrom(final ConfigImpl config) {
        return config.shadingValues.getBoolean(this.name());
    }

    public void setTo(final ConfigImpl config, final boolean value) {
        config.shadingValues.put(this.name(), value);
    }

    public ReloadType tryToggleOnKeyRelease(final ConfigImpl config) {
        if (this.keyMapping().consumeReleased()) {
            this.setTo(config, !this.getFrom(config));
            return this.reloadType();
        } else {
            return ReloadType.NONE;
        }
    }
}

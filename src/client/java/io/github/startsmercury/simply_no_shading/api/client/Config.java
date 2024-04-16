package io.github.startsmercury.simply_no_shading.api.client;

import java.util.Objects;

/**
 * The Simply No Shading client configuration <abbr>(config)</abbr>.
 * <p>
 * The config object stores data used by Simply No Shading and is usually
 * serialized.
 *
 * @since 6.2.0
 */
public class Config implements Cloneable {
    /**
     * Default vanilla shading.
     * <p>
     * Creates a new config defaulted to look exactly like the default vanilla
     * Minecraft shading.
     *
     * @return the config for vanilla shading
     * @see #isVanilla()
     */
    public static Config defaultVanilla() {
        return new Config(true, true);
    }

    /**
     * Default old lighting.
     * <p>
     * Creates a new config defaulted to look similar to OptiFine's "old block
     * lighting" feature available through internal shaders.
     *
     * @return the config for old lighting shading
     * @see #isOldLighting()
     */
    public static Config defaultOldLighting() {
        return new Config(false, true);
    }

    private boolean blockShadingEnabled;
    private boolean cloudShadingEnabled;

    /**
     * Creates a new config with all shading disabled.
     */
    public Config() {}

    /**
     * Creates a new config with all shading explicitly set.
     */
    public Config(final boolean blockShadingEnabled, final boolean cloudShadingEnabled) {
        this.blockShadingEnabled = blockShadingEnabled;
        this.cloudShadingEnabled = cloudShadingEnabled;
    }

    /**
     * Block shading.
     * <p>
     * This covers most blocks except block entities like chests and blocks held
     * by entities. Block shading is the uniform darkening of surfaces to give
     * perception of directional lighting from the sun. Disabling will remove
     * said darkening.
     *
     * @see #setBlockShadingEnabled(boolean)
     * @see #withBlockShadingEnabled(boolean)
     * @see #toggleBlockShading()
     * @see #withBlockShadingToggled()
     */
    public boolean blockShadingEnabled() {
        return this.blockShadingEnabled;
    }

    /**
     * Cloud shading.
     * <p>
     * Cloud shading is the uniform darkening of cloud planes to give perception
     * of directional lighting from the sun. Disabling will remove said
     * darkening.
     *
     * @see #setCloudShadingEnabled(boolean)
     * @see #withCloudShadingEnabled(boolean)
     * @see #toggleCloudShading()
     * @see #withCloudShadingToggled()
     */
    public boolean cloudShadingEnabled() {
        return this.cloudShadingEnabled;
    }

    /**
     * Sets block shading enabled or disabled.
     *
     * @see #blockShadingEnabled()
     */
    public void setBlockShadingEnabled(final boolean enabled) {
        this.blockShadingEnabled = enabled;
    }

    /**
     * Sets block shading enabled or disabled.
     *
     * @see #blockShadingEnabled()
     */
    public Config withBlockShadingEnabled(final boolean enabled) {
        this.blockShadingEnabled = enabled;
        return this;
    }

    /**
     * Sets block shading to its opposite value.
     *
     * @see #blockShadingEnabled()
     * @see #setBlockShadingEnabled(boolean)
     */
    public void toggleBlockShading() {
        this.blockShadingEnabled ^= true;
    }

    /**
     * Sets block shading to its opposite value.
     *
     * @see #blockShadingEnabled()
     * @see #setBlockShadingEnabled(boolean)
     */
    public Config withBlockShadingToggled() {
        this.blockShadingEnabled ^= true;
        return this;
    }

    /**
     * Sets cloud shading enabled or disabled.
     *
     * @see #cloudShadingEnabled()
     */
    public void setCloudShadingEnabled(final boolean enabled) {
        this.cloudShadingEnabled = enabled;
    }

    /**
     * Sets cloud shading enabled or disabled.
     *
     * @see #cloudShadingEnabled()
     */
    public Config withCloudShadingEnabled(final boolean enabled) {
        this.cloudShadingEnabled = enabled;
        return this;
    }

    /**
     * Sets cloud shading to its opposite value.
     *
     * @see #cloudShadingEnabled()
     * @see #setCloudShadingEnabled(boolean)
     */
    public void toggleCloudShading() {
        this.cloudShadingEnabled ^= true;
    }

    /**
     * Sets cloud shading to its opposite value.
     *
     * @see #cloudShadingEnabled()
     * @see #setCloudShadingEnabled(boolean)
     */
    public Config withCloudShadingToggled() {
        this.cloudShadingEnabled ^= true;
        return this;
    }

    /**
     * Default vanilla shading.
     * <p>
     * Vanilla shading config is a config that would result in behavior
     * observable in the absence of Simply No Shading.
     *
     * @see #defaultVanilla()
     * @see #setVanilla()
     * @see #withVanilla()
     */
    public boolean isVanilla() {
        return this.blockShadingEnabled && this.cloudShadingEnabled;
    }

    /**
     * Default old lighting.
     * <p>
     * Old lighting shading config is a config that would result in behavior
     * observable when playing with OptiFine's old lighting accessible through
     * specific configurations to internal shaders.
     *
     * @see #defaultOldLighting()
     * @see #setOldLighting()
     * @see #withOldLighting()
     */
    public boolean isOldLighting() {
        return !this.blockShadingEnabled && this.cloudShadingEnabled;
    }

    /**
     * Absolute default.
     * <p>
     * An absolute default config is simply a config with defaulted internal
     * values. With shading toggles, this means all types of shading are
     * disabled.
     *
     * @see #Config()
     * @see #setDefault()
     * @see #withDefault()
     */
    public boolean isDefault() {
        return !(this.blockShadingEnabled || this.cloudShadingEnabled);
    }

    /**
     * Sets the values of this config for vanilla shading.
     *
     * @see #isVanilla()
     */
    public void setVanilla() {
        this.blockShadingEnabled = true;
        this.cloudShadingEnabled = true;
    }

    /**
     * Sets the values of this config for vanilla shading.
     *
     * @see #isVanilla()
     */
    public Config withVanilla() {
        this.blockShadingEnabled = true;
        this.cloudShadingEnabled = true;
        return this;
    }

    /**
     * Sets the values of this config for old lighting.
     *
     * @see #isOldLighting()
     */
    public void setOldLighting() {
        this.blockShadingEnabled = false;
        this.cloudShadingEnabled = true;
    }

    /**
     * Sets the values of this config for old lighting.
     *
     * @see #isOldLighting()
     */
    public Config withOldLighting() {
        this.blockShadingEnabled = false;
        this.cloudShadingEnabled = true;
        return this;
    }

    /**
     * Sets the values of this config to absolute default.
     *
     * @see #isDefault()
     */
    public void setDefault() {
        this.blockShadingEnabled = false;
        this.cloudShadingEnabled = false;
    }

    /**
     * Sets the values of this config to absolute default.
     *
     * @see #isDefault()
     */
    public Config withDefault() {
        this.blockShadingEnabled = false;
        this.cloudShadingEnabled = false;
        return this;
    }

    /**
     * Creates a text representation of the accessible components of this
     * config.
     * <p>
     * The component string representation is usually used for debugging the
     * state of the config, or part of it. This is not the usual method used
     * in (de)serializing config objects.
     * <p>
     * {@inheritDoc}
     *
     * @see #toString()
     */
    public String toComponentString() {
        return "[blockShadingEnabled="
            + this.blockShadingEnabled
            + ",cloudShadingEnabled="
            + this.cloudShadingEnabled
            + "]";
    }

    /**
     * Creates a clone of this config.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Config clone() {
        try {
            return (Config) super.clone();
        } catch (final CloneNotSupportedException cause) {
            throw new InternalError("probably forgot Cloneable", cause);
        }
    }

    /**
     * Clones value from another.
     *
     * Cloning usually involve creating objects but when that is undesirable
     * this method may replace that by cloning values into a preexisting object.
     *
     * @see #clone()
     */
    public void cloneFrom(final Config source) {
        Objects.requireNonNull(source, "Parameter source is null");
        this.blockShadingEnabled = source.blockShadingEnabled;
        this.cloudShadingEnabled = source.cloudShadingEnabled;
    }

    /**
     * Performs equality check with this config and another object.
     * <p>
     * Compares the equality of this and another config and will only result to
     * {@code true} if the other config is of the same type and both their
     * internal values that are at least have getters to are also equal;
     * {code false} otherwise.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Config other) {
            return this.blockShadingEnabled == other.blockShadingEnabled
                && this.cloudShadingEnabled == other.cloudShadingEnabled;
        } else {
            return false;
        }
    }

    /**
     * Calculates the hash of this config.
     * <p>
     * The hash of this config is influenced by the values internal to this
     * object that are also accessible through getters.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            this.blockShadingEnabled,
            this.cloudShadingEnabled
        );
    }

    /**
     * Creates a verbose string representation of this config.
     * <p>
     * The string representation of this config will contain internal values
     * that are accessible through getters.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Config.class.getName() + toComponentString();
    }
}

package io.github.startsmercury.simply_no_shading.client;

import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;

/**
 * The Simply No Shading client configuration <abbr>(config)</abbr>..
 * <p>
 * The config object functions to store data following the Object-Oriented
 * Pattern (OOP). For example, config instance accesses can be easily wrapped
 * or accesses be synchronized (refer to {@code SimplyNoShading}'s
 * implementation).
 *
 * @since 6.2.0
 */
public class Config implements Cloneable {
    /**
     * Default vanilla shading.
     * <p>
     * Creates a new config defaulted to look exactly like the public vanilla
     * Minecraft shading.
     *
     * @return the config for vanilla shading
     * @since 6.2.0
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
     * @since 6.2.0
     * @see #isOldLighting()
     */
    public static Config defaultOldLighting() {
        return new Config(false, true);
    }

    private boolean blockShadingEnabled;
    private boolean cloudShadingEnabled;
    private boolean debugFileSyncEnabled = false;
    private boolean interactiveReloadEnabled = true;

    /**
     * Creates a new config with all shading disabled.
     */
    public Config() {}

    /**
     * Creates a new config with all shading explicitly set.
     */
    public Config(
        final boolean blockShadingEnabled,
        final boolean cloudShadingEnabled
    ) {
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
     * @since 6.2.0
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
     * @since 6.2.0
     * @see #setCloudShadingEnabled(boolean)
     * @see #withCloudShadingEnabled(boolean)
     * @see #toggleCloudShading()
     * @see #withCloudShadingToggled()
     */
    public boolean cloudShadingEnabled() {
        return this.cloudShadingEnabled;
    }

    /**
     * Debug config file syncing.
     * <p>
     * A usually debug purposed option to load config changes and save changes.
     * Unlike when disabled where the config is only ever loaded once at the
     * start or through a key mapping and saved after applying changes int the
     * configuration screen.
     *
     * @since 6.2.0
     * @see #setDebugFileSyncEnabled(boolean)
     */
    @ApiStatus.Experimental
    public boolean debugFileSyncEnabled() {
        return this.debugFileSyncEnabled;
    }

    /**
     * Interactive reload in configuration screen.
     * <p>
     * When enabled, changes are applied immediately instead of after exiting
     * the screen.
     *
     * @since 6.2.0
     */
    @ApiStatus.Experimental
    public boolean interactiveReloadEnabled() {
        return this.interactiveReloadEnabled;
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
     * @since 6.2.0
     * @see #blockShadingEnabled()
     */
    public Config withBlockShadingEnabled(final boolean enabled) {
        this.blockShadingEnabled = enabled;
        return this;
    }

    /**
     * Sets block shading to its opposite value.
     *
     * @since 6.2.0
     * @see #blockShadingEnabled()
     * @see #setBlockShadingEnabled(boolean)
     */
    public void toggleBlockShading() {
        this.blockShadingEnabled ^= true;
    }

    /**
     * Sets block shading to its opposite value.
     *
     * @since 6.2.0
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
     * @since 6.2.0
     * @see #cloudShadingEnabled()
     */
    public Config withCloudShadingEnabled(final boolean enabled) {
        this.cloudShadingEnabled = enabled;
        return this;
    }

    /**
     * Sets cloud shading to its opposite value.
     *
     * @since 6.2.0
     * @see #cloudShadingEnabled()
     * @see #setCloudShadingEnabled(boolean)
     */
    public void toggleCloudShading() {
        this.cloudShadingEnabled ^= true;
    }

    /**
     * Sets cloud shading to its opposite value.
     *
     * @since 6.2.0
     * @see #cloudShadingEnabled()
     * @see #setCloudShadingEnabled(boolean)
     */
    public Config withCloudShadingToggled() {
        this.cloudShadingEnabled ^= true;
        return this;
    }

    /**
     * Sets debug config file syncing enabled.
     *
     * @since 6.2.0
     * @see #debugFileSyncEnabled()
     */
    @ApiStatus.Experimental
    public void setDebugFileSyncEnabled(final boolean enabled) {
        this.debugFileSyncEnabled = enabled;
    }

    /**
     * Sets nteractive reload in configuration screen enabled.
     *
     * @since 6.2.0
     * @see #interactiveReloadEnabled()
     */
    @ApiStatus.Experimental
    public void setInteractiveReloadEnabled(final boolean enabled) {
        this.interactiveReloadEnabled = enabled;
    }

    /**
     * Default vanilla shading.
     * <p>
     * Vanilla shading config is a config that would result in behavior
     * observable in the absence of Simply No Shading.
     *
     * @since 6.2.0
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
     * @since 6.2.0
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
     * @since 6.2.0
     * @see #Config()
     * @see #setDefault()
     * @see #withDefault()
     */
    public boolean isDefault() {
        return !(this.blockShadingEnabled || this.cloudShadingEnabled);
    }

    /**
     * Sets the values of this config for vanilla shading.
     * @since 6.2.0
     * @see #isVanilla()
     */
    public void setVanilla() {
        this.blockShadingEnabled = true;
        this.cloudShadingEnabled = true;
    }

    /**
     * Sets the values of this config for vanilla shading.
     *
     * @since 6.2.0
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
     * @since 6.2.0
     * @see #isOldLighting()
     */
    public void setOldLighting() {
        this.blockShadingEnabled = false;
        this.cloudShadingEnabled = true;
    }

    /**
     * Sets the values of this config for old lighting.
     *
     * @since 6.2.0
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
     * @since 6.2.0
     * @see #isDefault()
     */
    public void setDefault() {
        this.blockShadingEnabled = false;
        this.cloudShadingEnabled = false;
    }

    /**
     * Sets the values of this config to absolute default.
     *
     * @since 6.2.0
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
     * Creates a clone or shallow copy of this config.
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
     * Updates this config to clone or shallowly copy the value of another.
     *
     * @since 6.2.0
     * @see #clone()
     */
    public void cloneFrom(final Config source) {
        Objects.requireNonNull(source, "Parameter source is null");
        this.blockShadingEnabled = source.blockShadingEnabled;
        this.cloudShadingEnabled = source.cloudShadingEnabled;
        this.debugFileSyncEnabled = source.debugFileSyncEnabled;
        this.interactiveReloadEnabled = source.interactiveReloadEnabled;
    }

    /**
     * Performs equality check with this config and another object.
     * <p>
     * Though this method could accept any objects of any type, only another
     * config with the exact same values would result with {@code true}.
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
     * The hash of the config is only determined by the internal values that has
     * some form of accessing publicly and that are not dynamically synthesized
     * or derived from simpler internal values.
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
     * The string representation contains internal values that have accessors or
     * getters. It's not the scope of this method to include the return of every
     * method.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Config.class.getName() + toComponentString();
    }
}

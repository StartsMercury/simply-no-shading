package io.github.startsmercury.simply_no_shading.api.client;

/**
 * Stores data used by this mod.
 * <p>
 * This class is normally serialized and deserialized to persist and restore
 * the same configuration data. Defaults should be preferred over nulls when
 * representing an initial, pre-set, or default data.
 *
 * @since 7.0.0
 */
public interface Config {
    /**
     * Gets block shading enabled.
     * <p>
     * Block shading being enabled or disabled affect liquids and blocks, excluding
     * block entities or use similar rendering to, in unmodified Minecraft.
     *
     * @see #setBlockShadingEnabled(boolean)
     * @return block shading enabled
     */
    boolean blockShadingEnabled();

    /**
     * Sets block shading enabled or disabled.
     *
     * @see #blockShadingEnabled()
     */
    void setBlockShadingEnabled(boolean enabled);

    /**
     * Cloud shading.
     * <p>
     * Cloud shading being enabled or disabled affect clouds in unmodified Minecraft.
     *
     * @see #setCloudShadingEnabled(boolean)
     * @return cloud shading enabled
     */
    boolean cloudShadingEnabled();

    /**
     * Sets cloud shading enabled or disabled.
     *
     * @see #cloudShadingEnabled()
     */
    void setCloudShadingEnabled(boolean enabled);
}

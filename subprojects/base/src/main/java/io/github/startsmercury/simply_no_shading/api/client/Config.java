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
     * @return block shading enabled
     * @see #setBlockShadingEnabled(boolean)
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
     * @return cloud shading enabled
     * @see #setCloudShadingEnabled(boolean)
     */
    boolean cloudShadingEnabled();

    /**
     * Sets cloud shading enabled or disabled.
     *
     * @see #cloudShadingEnabled()
     */
    void setCloudShadingEnabled(boolean enabled);

    /**
     * Gets entity shading enabled.
     * <p>
     * Entity shading being enabled or disabled affect entities and block entities.
     * This would also include the hand and held items.
     *
     * @return entity shading enabled
     * @since 7.4.0
     * @see #setEntityShadingEnabled(boolean)
     */
    boolean entityShadingEnabled();

    /**
     * Sets entity shading enabled or disabled.
     *
     * @see #entityShadingEnabled()
     * @since 7.4.0
     */
    void setEntityShadingEnabled(boolean enabled);
}

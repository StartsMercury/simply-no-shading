package io.github.startsmercury.simply_no_shading.api.client;

/**
 * The Simply No Shading client configuration.
 * <p>
 * A concrete instance of this class stores data used by Simply No Shading and
 * is usually serialized.
 *
 * @since 7.0.0
 */
public interface Config {
    /**
     * Block shading.
     *
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
     *
     * @see #setCloudShadingEnabled(boolean)
     */
    boolean cloudShadingEnabled();

    /**
     * Sets cloud shading enabled or disabled.
     *
     * @see #cloudShadingEnabled()
     */
    void setCloudShadingEnabled(boolean enabled);
}

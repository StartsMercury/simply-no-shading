package io.github.startsmercury.simply_no_shading.api.client;

import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Simply No Shading minecraft mod public interface.
 *
 * @since 7.0.0
 */
public interface SimplyNoShading {
    /**
     * The Simply No Shading implementation instance.
     * <p>
     * It is discouraged for this method to return different instances
     * for an implementation as callers may validly assume that the
     * first valid instance is a singleton.
     *
     * @implSpec It is possible to call this method before an instance is
     *     present which may result in a
     *     {@linkplain RuntimeException runtime exception}.
     */
    static @NotNull SimplyNoShading instance() {
        return io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl.instance();
    }

    /**
     * Gets the config path.
     * <p>
     * This path is where the config will be stored and read from or null when not set.
     * <p>
     * This is an optional operation.
     *
     * @return the client configuration file path
     * @throws UnsupportedOperationException if getting the config path is not supported
     */
    default @Nullable Path configPath() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("configPath");
    }

    /**
     * Gets the config.
     * <p>
     * This mod's config is used for customization, determining which features
     * should be expressed, how, or by how much.
     *
     * @return the config
     * @see #setConfig(Config)
     */
    @NotNull Config config();

    /**
     * Set the config by copying.
     * <p>
     * Changes the config by copying from another value.
     *
     * @param config the config to copy values from
     * @throws NullPointerException if the config is null
     * @see #config()
     */
    void setConfig(@NotNull Config config) throws NullPointerException;
}

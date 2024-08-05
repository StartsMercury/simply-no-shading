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
     * Gets the singleton implementation of this class.
     *
     * @return an instance of this class
     * @throws RuntimeException if the single instance is not yet available
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

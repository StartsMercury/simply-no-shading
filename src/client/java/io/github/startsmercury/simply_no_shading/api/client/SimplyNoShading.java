package io.github.startsmercury.simply_no_shading.api.client;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simply No Shading.
 * <p>
 * A concrete instance of this class represents the state of Simply No Shading
 * as a Minecraft modification.
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
        final var instance = io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl.instance;
        if (instance != null) {
            return instance;
        } else {
            throw new RuntimeException("Simply No Shading is not yet initialized");
        }
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
     * The Simply No Shading config.
     * <p>
     * This returns a non-{@code null} read-only view or copy of config. The
     * returned config must not affect later calls to this method.
     *
     * @see #setConfig(Config)
     */
    @NotNull Config config();

    /**
     * Changes the config by copying values.
     * <p>
     * Copies another config such that changes from it will not be reflected
     * (see {@link #config()}).
     *
     * @param config the config to copy values from
     * @see #config()
     * @throws NullPointerException when config is {@code null}.
     */
    void setConfig(@NotNull Config config);
}

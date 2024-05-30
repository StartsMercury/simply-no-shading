package io.github.startsmercury.simply_no_shading.api.client;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

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
        final var instance = io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl.instance;
        if (instance != null) {
            return instance;
        } else {
            throw new RuntimeException("Simply No Shading is not yet initialized");
        }
    }

    /**
     * Simply No Shading's client configuration file path.
     * <p>
     * This method returns the same non-{@code null} {@link Path path} to the
     * client configuration file.
     *
     * @return the client configuration file path
     */
    @NotNull Path configPath();

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

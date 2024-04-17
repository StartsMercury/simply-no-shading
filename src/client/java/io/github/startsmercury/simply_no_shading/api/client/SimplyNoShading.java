package io.github.startsmercury.simply_no_shading.api.client;

import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShadingImpl;
import java.nio.file.Path;

/**
 * Simply No Shading.
 * <p>
 * A concrete instance of this class represents the state of Simply No Shading
 * as a Minecraft modification.
 *
 * @since 6.2.0
 */
public interface SimplyNoShading {
    /**
     * The Simply No Shading implementation instance.
     *
     * @implSpec This method may be implemented such that it returns different
     *           implementation instance, but this is discouraged as callers may
     *           validly assume to receive the same reference.
     * @implNote It is possible to call this method before the instance is
     *           initialized, which will result in a {@link RuntimeException}.
     */
    static SimplyNoShading instance() {
        final var instance = SimplyNoShadingImpl.instance;
        if (instance != null) {
            return instance;
        } else {
            throw new RuntimeException("Simply No Shading is not yet initialized");
        }
    }

    /**
     * Simply No Shading's client configuration file path.
     *
     * @implSpec This method return may vary but it is discouraged and should
     *           never be {@code null}.
     */
    Path configPath();

    /**
     * The Simply No Shading config.
     *
     * @see #setConfig(Config)
     * @implSpec This method may return any non-{@code null} concrete
     *           implementation of {@link Config} and it is recommended that
     *           the returned config instance has no possible avenue to
     *           influence succeeding calls such as by returning a copy instead.
     */
    Config config();

    /**
     * Sets the main config.
     *
     * @see #config()
     * @implSpec This method may throw on {@code null}s and may store the passed
     *           config instance, but it is preferred to store a copy of the
     *           value instead.
     */
    void setConfig(Config config);
}

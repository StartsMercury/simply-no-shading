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
     *
     * @implSpec It is discouraged for this method to return different instances
     *           for an implementation as callers may validly assume that the
     *           first valid instance is a singleton.
     * @implNote It is possible to call this method before the instance is
     *           initialized, which may result in a
     *           {@linkplain RuntimeException runtime exception}.
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
     *
     * @implSpec This method should preferably return the same path and should
     *           never be {@code null}.
     */
    Path configPath();

    /**
     * The Simply No Shading config.
     *
     * @see #setConfig(Config)
     * @implSpec This method may return any non-{@code null} concrete
     *           implementation of {@link Config} and preferrably carry no
     *           side-effects to future calls to this method.
     */
    @NotNull Config config();

    /**
     * Changes the config.
     *
     * @see #config()
     * @implSpec This method may throw an exception on {@code null} values and
     *           preferable copy the values to avoid untrivial side-effects.
     */
    void setConfig(Config config);
}

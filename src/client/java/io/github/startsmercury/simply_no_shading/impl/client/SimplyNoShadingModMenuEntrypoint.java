package io.github.startsmercury.simply_no_shading.impl.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * The Simply No Shading {@link ModMenuApi modmenu} initializer.
 *
 * @see ModMenuApi
 * @since 6.2.0
 */
public final class SimplyNoShadingModMenuEntrypoint implements ModMenuApi {
    /**
     * The Simply No Shading client config screen factory.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        final var simplyNoShading = SimplyNoShadingImpl.instance;
        return lastScreen -> new ConfigScreen(lastScreen, simplyNoShading.config());
    }
}

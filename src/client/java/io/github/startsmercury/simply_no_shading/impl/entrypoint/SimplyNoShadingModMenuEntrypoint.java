package io.github.startsmercury.simply_no_shading.impl.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.startsmercury.simply_no_shading.impl.client.gui.screens.ConfigScreen;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;

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
        final var simplyNoShading = SimplyNoShading.instance();
        return lastScreen -> new ConfigScreen(lastScreen, simplyNoShading.config());
    }
}

package io.github.startsmercury.simply_no_shading.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.startsmercury.simply_no_shading.client.gui.screens.ConfigScreen;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShading;

/**
 * The Simply No Shading {@link ModMenuApi modmenu} initializer.
 * <p>
 * Implements inter-operability with Mod Menu to provide user centered features,
 * specifically making the config screen button available in the mod menu.
 *
 * @since 6.2.0
 * @see ModMenuApi
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

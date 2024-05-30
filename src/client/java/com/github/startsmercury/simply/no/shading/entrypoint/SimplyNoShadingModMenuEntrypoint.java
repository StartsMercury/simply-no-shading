package com.github.startsmercury.simply.no.shading.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * The {@code SimplyNoShadingModMenuEntrypoint} class is an implementation of
 * {@link ModMenuApi} and is an entrypoint defined with the {@code modmenu} key
 * in the {@code fabric.mod.json}. This acts as compatibility between Simply No
 * Shading and ModMenu.
 *
 * @since 6.0.0
 * @deprecated No replacement
 */
@Deprecated(since = "7.0.0", forRemoval = true)
@SuppressWarnings({ "all", "removal" })
public class SimplyNoShadingModMenuEntrypoint implements ModMenuApi {
    private final ModMenuApi delegate;

    public SimplyNoShadingModMenuEntrypoint() {
        this.delegate =
            new io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingModMenuEntrypoint();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return delegate.getModConfigScreenFactory();
    }
}

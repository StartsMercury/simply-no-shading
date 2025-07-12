package com.github.startsmercury.simply.no.shading.entrypoint;

import io.github.prospector.modmenu.api.ModMenuApi;
import java.util.function.Function;
import net.minecraft.client.gui.screens.Screen;

/**
 * The {@code SimplyNoShadingModMenuEntrypoint} class is an implementation of
 * {@link ModMenuApi} and is an entrypoint defined with the {@code modmenu} key
 * in the {@code fabric.mod.json}. This acts as compatibility between Simply No
 * Shading and ModMenu.
 *
 * @since 6.0.0
 * @deprecated For removal since 7.0.0 with no replacement
 */
@Deprecated
@SuppressWarnings("all")
public class SimplyNoShadingModMenuEntrypoint implements ModMenuApi {
    private final ModMenuApi delegate;

    public SimplyNoShadingModMenuEntrypoint() {
        this.delegate =
            new io.github.startsmercury.simply_no_shading.impl.client.entrypoint.SimplyNoShadingModMenu();
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return this.delegate.getConfigScreenFactory();
    }

    @Override
    public String getModId() {
        return this.delegate.getModId();
    }
}

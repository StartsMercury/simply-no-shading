package io.github.startsmercury.simply_no_shading.impl.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.gui.screens.ConfigScreen;
import java.util.function.Function;
import net.minecraft.client.gui.screens.Screen;

public final class SimplyNoShadingModMenuEntrypoint implements ModMenuApi {
    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return lastScreen -> new ConfigScreen(lastScreen, SimplyNoShading.instance().config());
    }

    @Override
    public String getModId() {
        return "simply-no-shading";
    }
}

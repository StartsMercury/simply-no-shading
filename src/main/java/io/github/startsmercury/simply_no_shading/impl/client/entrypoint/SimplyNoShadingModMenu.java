package io.github.startsmercury.simply_no_shading.impl.client.entrypoint;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.Minecraft;

public final class SimplyNoShadingModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return lastScreen -> {
            final Minecraft minecraft = Minecraft.getInstance();
            final SimplyNoShadingImpl simplyNoShading = (SimplyNoShadingImpl) minecraft.getSimplyNoShading();
            return simplyNoShading.createConfigScreen(lastScreen);
        };
    }
}

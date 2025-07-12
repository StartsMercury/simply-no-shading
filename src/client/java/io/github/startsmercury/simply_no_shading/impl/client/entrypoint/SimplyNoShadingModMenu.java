package io.github.startsmercury.simply_no_shading.impl.client.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.Minecraft;

public final class SimplyNoShadingModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return lastScreen -> {
            final var minecraft = Minecraft.getInstance();
            final var simplyNoShading = (SimplyNoShadingImpl) minecraft.getSimplyNoShading();
            return simplyNoShading.createConfigScreen(lastScreen);
        };
    }
}

package io.github.startsmercury.simply_no_shading.impl.client.entrypoint;

import io.github.prospector.modmenu.api.ModMenuApi;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public final class SimplyNoShadingModMenu implements ModMenuApi {
    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return lastScreen -> {
            final Minecraft minecraft = Minecraft.getInstance();
            final SimplyNoShadingImpl simplyNoShading = (SimplyNoShadingImpl) minecraft.getSimplyNoShading();
            return simplyNoShading.createConfigScreen(lastScreen);
        };
    }

    @Override
    public String getModId() {
        return SnsConstants.NAME;
    }
}

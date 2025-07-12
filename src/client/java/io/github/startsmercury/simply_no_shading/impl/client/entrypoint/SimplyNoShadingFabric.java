package io.github.startsmercury.simply_no_shading.impl.client.entrypoint;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

public class SimplyNoShadingFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        final var simplyNoShading =
            (SimplyNoShadingImpl) Minecraft.getInstance().getSimplyNoShading();

        simplyNoShading.onInitialize();
    }
}

package io.github.startsmercury.simply_no_shading.entrypoint;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.startsmercury.simply_no_shading.client.ReloadType;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils;
import io.github.startsmercury.simply_no_shading.client.gui.screens.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.resources.ResourceLocation;

/**
 * The Simply No Shading client-side entrypoint.
 *
 * @since 6.2.0
 * @see ClientModInitializer
 */
public final class SimplyNoShadingClientEntrypoint implements ClientModInitializer {
    /**
     * Initializes the Simply No Shading client.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void onInitializeClient() {
        final var simplyNoShading = SimplyNoShading.instance();
        SimplyNoShadingUtils.tryLoadConfig(simplyNoShading);

        this.registerKeyMappings(simplyNoShading);
        this.registerResources();
        this.registerShutdownHook(() -> SimplyNoShadingUtils.trySaveConfig(simplyNoShading));
    }

    private void registerKeyMappings(final SimplyNoShading simplyNoShading) {
        final var openConfigScreen = new KeyMapping(
            "simply-no-shading.key.openConfigScreen",
            InputConstants.UNKNOWN.getValue(),
            "simply-no-shading.key.categories.simply-no-shading"
        );
        final var reloadConfig = new KeyMapping(
            "simply-no-shading.key.reloadConfig",
            InputConstants.UNKNOWN.getValue(),
            "simply-no-shading.key.categories.simply-no-shading"
        );

        // NOTE: SimplyNoShading::config returns a copy, not a reference
        final var toggleBlockShading = new ToggleKeyMapping(
            "simply-no-shading.key.toggleBlockShading",
            InputConstants.UNKNOWN.getValue(),
            "simply-no-shading.key.categories.simply-no-shading",
            () -> simplyNoShading.config().blockShadingEnabled()
        );
        final var toggleCloudShading = new ToggleKeyMapping(
            "simply-no-shading.key.toggleCloudShading",
            InputConstants.UNKNOWN.getValue(),
            "simply-no-shading.key.categories.simply-no-shading",
            () -> simplyNoShading.config().cloudShadingEnabled()
        );

        KeyBindingHelper.registerKeyBinding(openConfigScreen);
        KeyBindingHelper.registerKeyBinding(reloadConfig);
        KeyBindingHelper.registerKeyBinding(toggleBlockShading);
        KeyBindingHelper.registerKeyBinding(toggleCloudShading);

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (minecraft.screen instanceof ConfigScreen) {
                // Avoid overlapping functionality
                return;
            }

            if (
                !SimplyNoShadingUtils.consumeConfigWatchEvents(simplyNoShading)
                    && reloadConfig.isDown()
            ) {
                SimplyNoShadingUtils.tryLoadConfig(simplyNoShading);
            }

            if (openConfigScreen.isDown()) {
                minecraft.setScreen(new ConfigScreen(null, simplyNoShading.config()));
                return;
            }

            final var config = simplyNoShading.config();
            final var reloadType = ReloadType.max(
                SimplyNoShadingClientEntrypoint.cycleInConfig(
                    toggleBlockShading,
                    ReloadType.Major,
                    config::toggleBlockShading
                ),
                SimplyNoShadingClientEntrypoint.cycleInConfig(
                    toggleCloudShading,
                    ReloadType.Minor,
                    config::toggleCloudShading
                )
            );
            if (reloadType == ReloadType.None)
                return;
            reloadType.applyTo(minecraft.levelRenderer);
            simplyNoShading.setConfig(config);
            if (config.debugFileSyncEnbled()) {
                SimplyNoShadingUtils.trySaveConfig();
            }
        });
    }

    private static ReloadType cycleInConfig(
        final KeyMapping key,
        final ReloadType reloadType,
        final Runnable action
    ) {
        if (key.isDown()) {
            action.run();
            return reloadType;
        } else {
            return ReloadType.None;
        }
    }

    private void registerResources() {
        FabricLoader.getInstance()
            .getModContainer("simply-no-shading")
            .ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(
                new ResourceLocation("simply-no-shading", "simply_no_entity_like_shading"),
                container,
                ResourcePackActivationType.NORMAL
            ));
    }

    private void registerShutdownHook(final Runnable shutdownAction) {
        final var shutdownThread = new Thread(shutdownAction);
        shutdownThread.setName("Simply No Shading Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}

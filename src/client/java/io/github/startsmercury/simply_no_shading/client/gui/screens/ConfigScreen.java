package io.github.startsmercury.simply_no_shading.client.gui.screens;

import io.github.startsmercury.simply_no_shading.client.Config;
import io.github.startsmercury.simply_no_shading.client.ReloadType;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils.ComputedConfig;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;

/**
 * The Simply No Shading config screen.
 *
 * @since 6.2.0
 */
public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");
    private final Config config;
    private OptionsList list;
    private ReloadType reloadType;

    /**
     * Creates a new config screen given the last screen and the config.
     *
     * @param lastScreen the screen to open when this one closes
     * @param config the config reference being edited
     */
    public ConfigScreen(final Screen lastScreen, final Config config) {
        super(lastScreen, null, ConfigScreen.TITLE);

        Objects.requireNonNull(config, "Parameter config is null");

        this.config = config;
        this.reloadType = ReloadType.None;
    }

    @Override
    protected void init() {
        this.list = this.addRenderableWidget(
            new OptionsList(this.minecraft, this.width, this.height, this)
        );

        this.list.addSmall(
            this.createToggleButton(
                "simply-no-shading.config.option.blockShadingEnabled",
                this.config.blockShadingEnabled(),
                this.config::setBlockShadingEnabled,
                ReloadType.Major
            ),
            this.createToggleButton(
                "simply-no-shading.config.option.cloudShadingEnabled",
                this.config.cloudShadingEnabled(),
                this.config::setCloudShadingEnabled,
                ReloadType.Minor
            )
        );
        this.list.addSmall(this.createToEntityLikeShadingButton(), null);
        this.list.addSmall(
            this.createToggleButton(
                "simply-no-shading.config.option.debugFileSyncEnbled",
                this.config.debugFileSyncEnbled(),
                enabled -> {
                    this.config.setDebugFileSyncEnbled(enabled);
                    ComputedConfig.setDebugFileSyncEnbled(enabled);
                },
                ReloadType.None
            ),
            this.createToggleButton(
                "simply-no-shading.config.option.interactiveReloadEnabled",
                this.config.interactiveReloadEnabled(),
                this.config::setInteractiveReloadEnabled,
                ReloadType.None
            )
        );

        super.init();
    }

    private OptionInstance<Boolean> createToggleButton(
        final String key,
        final boolean initial,
        final BooleanConsumer setter,
        final ReloadType reloadType
    ) {
        final var tooltip = Tooltip.create(Component.translatable(key + ".tooltip"));
        return OptionInstance.createBoolean(
            key,
            enabled -> tooltip,
            initial,
            enabled -> {
                setter.accept((boolean) enabled);
                final var queuedReloadType = this.reloadType.max(reloadType);

                final var debugFileSyncEnbled = this.config.debugFileSyncEnbled();
                final var interactiveReloadEnabled = queuedReloadType != ReloadType.None
                    && this.config.interactiveReloadEnabled();

                if (debugFileSyncEnbled || interactiveReloadEnabled) {
                    SimplyNoShading.instance().setConfig(this.config);
                }
                if (debugFileSyncEnbled) {
                    SimplyNoShadingUtils.trySaveConfig(SimplyNoShading.instance());
                }
                if (interactiveReloadEnabled) {
                    final var levelRenderer = this.minecraft().levelRenderer;
                    queuedReloadType.applyTo(levelRenderer);
                    this.reloadType = ReloadType.None;
                } else {
                    this.reloadType = queuedReloadType;
                }
            }
        );
    }

    private Button createToEntityLikeShadingButton() {
        final var minecraft = this.minecraft();
        return Button.builder(
            Component.translatable("simply-no-shading.config.option.experimentalEntityLikeShading"),
            self -> minecraft.setScreen(
                new PackSelectionScreen(
                    minecraft.getResourcePackRepository(),
                    packRepository -> {
                        minecraft.options.updateResourcePacks(packRepository);
                        minecraft.setScreen(this);
                    },
                    minecraft.getResourcePackDirectory(),
                    Component.translatable("resourcePack.title")
                )
            )
        ).build();
    }

    @Override
    public void tick() {
        final var simplyNoShading = SimplyNoShading.instance();
        if (
            this.config.debugFileSyncEnbled()
                && SimplyNoShadingUtils.discardConfigWatchEvents()
        ) {
            SimplyNoShadingUtils.tryLoadConfig(simplyNoShading);
            simplyNoShading.configInto(this.config);
            rebuildWidgets();
        }
    }

    @Override
    public void repositionElements() {
        super.repositionElements();
        if (this.list != null) {
            this.list.updateSize(this.width, this.layout);
        }
    }

    @Override
    public void removed() {
        final var simplyNoShading = SimplyNoShading.instance();
        simplyNoShading.setConfig(this.config);
        if (!config.debugFileSyncEnbled()) {
            SimplyNoShadingUtils.trySaveConfig(simplyNoShading);
        }
        if (!config.interactiveReloadEnabled()) {
            this.reloadType.applyTo(this.minecraft().levelRenderer);
        }
    }

    private Minecraft minecraft() {
        {
            final var minecraft = this.minecraft;
            if (minecraft != null) {
                return minecraft;
            }
        }

        System.err.println(
            this.getClass().getName() + " might not have been opened using Minecraft::setScreen"
        );
        return this.minecraft = Minecraft.getInstance();
    }
}

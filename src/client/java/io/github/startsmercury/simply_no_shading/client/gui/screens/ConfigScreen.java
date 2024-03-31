package io.github.startsmercury.simply_no_shading.client.gui.screens;

import io.github.startsmercury.simply_no_shading.client.Config;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils;
import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils.ComputedConfig;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;

import java.util.Objects;

/**
 * The Simply No Shading config screen.
 *
 * @since 6.2.0
 */
public final class ConfigScreen extends Screen {
    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");
    private final Config config;
    private final Screen lastScreen;
    private ReloadType reloadType;

    /**
     * Creates a new config screen given the last screen and the config.
     *
     * @param lastScreen the screen to open when this one closes
     * @param config the config reference being edited
     */
    public ConfigScreen(final Screen lastScreen, final Config config) {
        super(ConfigScreen.TITLE);

        Objects.requireNonNull(config, "Parameter config is null");

        this.config = config;
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        final var gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().padding(4, 4, 4, 0);

        final var rowHelper = gridLayout.createRowHelper(2);
        rowHelper.addChild(this.createToggleButton(
            "simply-no-shading.config.option.blockShadingEnabled",
            this.config.blockShadingEnabled(),
            this.config::setBlockShadingEnabled,
            ReloadType.Major
        ));
        rowHelper.addChild(this.createToggleButton(
            "simply-no-shading.config.option.cloudShadingEnabled",
            this.config.cloudShadingEnabled(),
            this.config::setCloudShadingEnabled,
            ReloadType.Minor
        ));
        rowHelper.addChild(this.createToEntityLikeShadingButton());
        rowHelper.addChild(
            this.createToggleButton(
                "simply-no-shading.config.option.debugFileSyncEnbled",
                this.config.debugFileSyncEnbled(),
                enabled -> {
                    this.config.setDebugFileSyncEnbled(enabled);
                    ComputedConfig.setDebugFileSyncEnbled(enabled);
                },
                ReloadType.None
            ),
            2 // introduce gap by incrementing index by two instead of one (ig)
        );
        rowHelper.addChild(this.createToggleButton(
            "simply-no-shading.config.option.interactiveReloadEnabled",
            this.config.interactiveReloadEnabled(),
            this.config::setInteractiveReloadEnabled,
            ReloadType.None
        ));

        gridLayout.arrangeElements();
        FrameLayout.alignInRectangle(gridLayout, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridLayout.visitWidgets(this::addRenderableWidget);
    }

    private CycleButton<Boolean> createToggleButton(
        final String key,
        final boolean initial,
        final BooleanConsumer setter,
        final ReloadType reloadType,
    ) {
        final var tooltip = Tooltip.create(Component.translatable(key + ".tooltip"));
        return CycleButton.onOffBuilder()
            .withInitialValue(initial)
            .withTooltip(enabled -> tooltip)
            .create(
                0,
                0,
                98,
                20,
                Component.translatable(key),
                (self, enabled) -> {
                    setter.accept((boolean) enabled);

                    final var debugFileSyncEnbled = this.config.debugFileSyncEnbled();
                    final var interactiveReloadEnabled = reloadType != ReloadType.None
                        && this.config.interactiveReloadEnabled();

                    if (debugFileSyncEnbled || interactiveReloadEnabled) {
                        SimplyNoShading.instance().setConfig(this.config);
                    }
                    if (debugFileSyncEnbled) {
                        SimplyNoShadingUtils.trySaveConfig();
                    }
                    if (interactiveReloadEnabled) {
                        final var levelRenderer = this.minecraft().levelRenderer;
                        this.reloadType.max(reloadType).applyTo(levelRenderer);
                        this.reloadType = ReloadType.None;
                    } else {
                        this.reloadType = this.reloadType.max(reloadType);
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
        ).width(98)
            .build();
    }

    @Override
    public void tick() {
        final var simplyNoShading = SimplyNoShading.instance();
        if (
            this.config.debugFileSyncEnbled()
                && SimplyNoShadingUtils.consumeConfigWatchEvents(simplyNoShading)
        ) {
            SimplyNoShadingUtils.tryLoadConfig(simplyNoShading);
            simplyNoShading.configInto(this.config);
            rebuildWidgets();
        }
    }

    @Override
    public void onClose() {
        this.minecraft().setScreen(this.lastScreen);
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

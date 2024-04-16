package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import static io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingUtils.LOGGER;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingUtils;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.impl.client.ReloadType;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
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
                this.reloadType = this.reloadType.max(reloadType);
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
        SimplyNoShadingUtils.trySaveConfig(simplyNoShading);
        this.reloadType.applyTo(this.minecraft().levelRenderer);
    }

    private Minecraft minecraft() {
        {
            final var minecraft = this.minecraft;
            if (minecraft != null) {
                return minecraft;
            }
        }

        LOGGER.warn(
            "[Simply No Shading] {} might not have been opened using Minecraft::setScreen",
            this
        );
        return this.minecraft = Minecraft.getInstance();
    }
}

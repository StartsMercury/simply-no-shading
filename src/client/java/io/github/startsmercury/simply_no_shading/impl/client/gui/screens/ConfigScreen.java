package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.ConfigImpl;
import io.github.startsmercury.simply_no_shading.impl.client.ReloadType;
import io.github.startsmercury.simply_no_shading.impl.client.ShadingToggle;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import java.util.Objects;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;

public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");
    private final ConfigImpl config;
    private OptionsList list;
    private ReloadType reloadType;

    public ConfigScreen(final Screen lastScreen, final Config config) {
        super(lastScreen, null, ConfigScreen.TITLE);

        Objects.requireNonNull(config, "Parameter config is null");

        this.config = new ConfigImpl(config);
        this.reloadType = ReloadType.NONE;
    }

    @Override
    protected void init() {
        this.list = new OptionsList(super.minecraft, this.width, this.height, this);
        this.addRenderableWidget(this.list);

        // Non-critical, stream could be fine here...
        final var shadingOptions = ((SimplyNoShadingImpl) SimplyNoShading.instance())
            .shadingToggles()
            .stream()
            .map(this::createShadingOption)
            .toArray(OptionInstance[]::new);
        this.list.addSmall(shadingOptions);

        this.list.addSmall(this.createToEntityLikeShadingButton(), null);

        super.init();
    }

    private OptionInstance<Boolean> createShadingOption(final ShadingToggle shadingToggle) {
        final var key = "simply-no-shading.config.option." + shadingToggle.name() + "Enabled";
        final var tooltip = Tooltip.create(Component.translatable(key + ".tooltip"));
        return OptionInstance.createBoolean(
            key,
            enabled -> tooltip,
            shadingToggle.getFrom(this.config),
            enabled -> {
                shadingToggle.setTo(this.config, enabled);
                this.reloadType = this.reloadType.compose(shadingToggle.reloadType());
            }
        );
    }

    private Button createToEntityLikeShadingButton() {
        final var minecraft = super.minecraft;
        assert minecraft != null;

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
        ((SimplyNoShadingImpl) simplyNoShading).saveConfig();

        final var minecraft = super.minecraft;
        assert minecraft != null;
        this.reloadType.applyTo(minecraft.levelRenderer);
    }
}

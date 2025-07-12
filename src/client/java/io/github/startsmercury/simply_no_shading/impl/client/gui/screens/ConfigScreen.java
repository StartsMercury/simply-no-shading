package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.impl.client.ConfigImpl;
import io.github.startsmercury.simply_no_shading.impl.client.ShadingTarget;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");

    private final ConfigImpl config;
    private OptionsList list;

    private final Consumer<? super Config> configCallback;

    public ConfigScreen(
        final Screen lastScreen,
        final Config config,
        final Consumer<? super Config> configCallback
    ) {
        super(lastScreen, null, ConfigScreen.TITLE);

        Objects.requireNonNull(config, "Parameter config is null");

        this.config = new ConfigImpl(config);
        this.configCallback = configCallback;
    }

    @Override
    protected void init() {
        this.list = new OptionsList(super.minecraft, this.width, this.height, this);
        this.addRenderableWidget(this.list);

        final var shadingOptions = ShadingTarget
            .valueList()
            .stream()
            .map(this::createShadingOption)
            .toArray(OptionInstance[]::new);
        this.list.addSmall(shadingOptions);

        super.init();
    }

    private OptionInstance<Boolean> createShadingOption(final ShadingTarget target) {
        final var key = "simply-no-shading.config.option." + target + "ShadingEnabled";
        final var tooltip = Tooltip.create(Component.translatable(key + ".tooltip"));
        return OptionInstance.createBoolean(
            key,
            enabled -> tooltip,
            target.getFrom(config),
            enabled -> target.setInto(config, enabled)
        );
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
        this.configCallback.accept(this.config);
    }
}

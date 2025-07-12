package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.impl.client.ConfigImpl;
import io.github.startsmercury.simply_no_shading.impl.client.ShadingTarget;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");

    private final ConfigImpl config;

    private final Consumer<? super Config> configCallback;

    public ConfigScreen(
        final Screen lastScreen,
        final Config config,
        final Consumer<? super Config> configCallback
    ) {
        super(lastScreen, Minecraft.getInstance().options, ConfigScreen.TITLE);

        Objects.requireNonNull(config, "Parameter config is null");

        this.config = new ConfigImpl(config);
        this.configCallback = configCallback;
    }

    @Override
    public void removed() {
        this.configCallback.accept(this.config);
    }

    @Override
    protected void addOptions() {
        final var list = this.list;
        assert list != null;

        final var shadingOptions = ShadingTarget
            .valueList()
            .stream()
            .map(this::createShadingOption)
            .toArray(OptionInstance[]::new);
        list.addSmall(shadingOptions);
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
}

package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigPreset;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");
    private static final Component GENERAL = Component.translatable("simply-no-shading.config.general.header");
    private static final Component PREFERENCES = Component.translatable("simply-no-shading.config.preferences.header");

    private final ConfigBuilder configBuilder;
    private final Consumer<? super Config> configCallback;

    public ConfigScreen(
        final @Nullable Screen lastScreen,
        final Config initialConfig,
        final Consumer<? super Config> configCallback
    ) {
        //noinspection DataFlowIssue there was a mistake, lastScreen IS Nullable
        super(lastScreen, Minecraft.getInstance().options, ConfigScreen.TITLE);

        Objects.requireNonNull(initialConfig, "Parameter initialConfig is null");

        this.configBuilder = new ConfigBuilder(initialConfig);
        this.configCallback = configCallback;
    }

    @Override
    public void removed() {
        this.configCallback.accept(this.configBuilder.build());
    }

    @Override
    protected void addOptions() {
        final var list = this.list;
        assert list != null;

        final var data = this.configBuilder.getCustom().orElse(ConfigData.DEFAULT);

        final var shadeBlocks = this.createBoolean("shadeBlocks", data.shadeBlocks(), this.configBuilder::setShadeBlocks);
        final var shadeClouds = this.createBoolean("shadeClouds", data.shadeClouds(), this.configBuilder::setShadeClouds);
        final var shadeEntities = this.createBoolean("shadeEntities", data.shadeEntities(), this.configBuilder::setShadeEntities);

        final var preset = new OptionInstance<>(
            "simply-no-shading.config.preset",
            OptionInstance.cachedConstantTooltip(
                Component.translatable("simply-no-shading.config.preset.tooltip")
            ),
            (component, p) -> Options.genericValueLabel(
                component,
                Component.translatable(
                    "simply-no-shading.config.preset."
                        + p.name().toLowerCase(Locale.ROOT)
                )
            ),
            new OptionInstance.SliderableEnum<>(ConfigPreset.valueList(), ConfigPreset.CODEC),
            ConfigPreset.CODEC,
            this.configBuilder.getPreset(),
            p -> {
                this.configBuilder.setPreset(p);
                this.presetChanged(list, shadeBlocks, shadeClouds, shadeEntities, p);
            }
        );

        list.addHeader(GENERAL);
        list.addSmall(this.createBoolean(
            "compatibilityMode",
            this.configBuilder.isCompatibilityMode(),
            this.configBuilder::setCompatibilityMode
        ));

        list.addHeader(PREFERENCES);
        list.addBig(preset);
        list.addSmall(shadeBlocks, shadeClouds);
        list.addSmall(shadeEntities);

        this.presetChanged(list, shadeBlocks, shadeClouds, shadeEntities, this.configBuilder.getPreset());
    }

    private void presetChanged(
        final OptionsList list,
        final OptionInstance<Boolean> shadeBlocks,
        final OptionInstance<Boolean> shadeClouds,
        final OptionInstance<Boolean> shadeEntities,
        final ConfigPreset preset
    ) {
        final var shadeBlocksWidget = list.findOption(shadeBlocks);
        final var shadeCloudsWidget = list.findOption(shadeClouds);
        final var shadeEntitiesWidget = list.findOption(shadeEntities);

        if (shadeBlocksWidget == null ||
            shadeCloudsWidget == null ||
            shadeEntitiesWidget == null
        ) return;

        preset.override().or(this.configBuilder::getCustom).ifPresent(data -> {
            trySetWidget(shadeBlocksWidget, data.shadeBlocks());
            trySetWidget(shadeCloudsWidget, data.shadeClouds());
            trySetWidget(shadeEntitiesWidget, data.shadeEntities());
        });

        shadeEntitiesWidget.active = // 3
            shadeCloudsWidget.active = // 2
                shadeBlocksWidget.active = // 1
                    preset.override().isEmpty(); // 0
    }

    private static <T> void trySetWidget(final AbstractWidget widget, final T value) {
        if (widget instanceof final CycleButton<?> button) {
            @SuppressWarnings("unchecked")
            final var unchecked = (CycleButton<T>) button;
            unchecked.setValue(value);
        }
    }

    private OptionInstance<Boolean> createBoolean(
        final String id,
        final boolean initial,
        final Consumer<Boolean> setter
    ) {
        final var key = "simply-no-shading.config." + id;
        final var tooltip = Tooltip.create(Component.translatable(key + ".tooltip"));
        return OptionInstance.createBoolean(key, b -> tooltip, initial, setter);
    }
}

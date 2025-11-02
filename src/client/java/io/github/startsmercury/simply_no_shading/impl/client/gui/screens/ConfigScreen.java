package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigPreset;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
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
import org.jetbrains.annotations.Nullable;

public final class ConfigScreen extends OptionsSubScreen {
    private static class Data {
        private boolean shadeBlocks;
        private boolean shadeClouds;
        private boolean shadeEntities;

        public Data(final ConfigData data) {
            this.shadeBlocks = data.shadeBlocks();
            this.shadeClouds = data.shadeClouds();
            this.shadeEntities = data.shadeEntities();
        }

        public ConfigData build() {
            return new ConfigData(this.shadeBlocks, this.shadeClouds, this.shadeEntities);
        }
    }

    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");
    private static final Component QUALITY_HEADER = Component.translatable("simply-no-shading.config.general.header");
    private static final Component PREFERENCES_HEADER = Component.translatable("simply-no-shading.config.preferences.header");

    private final Consumer<? super Config> configCallback;

    private ConfigPreset preset;
    private boolean compatibilityMode;
    private Optional<Data> custom;

    public ConfigScreen(
        final @Nullable Screen lastScreen,
        final Config initialConfig,
        final Consumer<? super Config> configCallback
    ) {
        //noinspection DataFlowIssue there was a mistake, lastScreen IS Nullable
        super(lastScreen, Minecraft.getInstance().options, ConfigScreen.TITLE);

        Objects.requireNonNull(initialConfig, "Parameter initialConfig is null");

        this.preset = initialConfig.preset();
        this.compatibilityMode = initialConfig.compatibilityMode();
        this.custom = initialConfig.custom().map(Data::new);

        this.configCallback = configCallback;
    }

    @Override
    public void removed() {
        this.configCallback.accept(new Config(
            this.compatibilityMode,
            this.preset,
            this.custom.map(Data::build)));
    }

    @Override
    protected void addOptions() {
        final var list = this.list;
        assert list != null;

        final var data = this.custom.map(Data::build).orElse(ConfigData.DEFAULT);

        final var shadeBlocks = this.createBoolean("shadeBlocks", data.shadeBlocks(), b -> this.getOrCreateCustom().shadeBlocks = b);
        final var shadeClouds = this.createBoolean("shadeClouds", data.shadeClouds(), b -> this.getOrCreateCustom().shadeClouds = b);
        final var shadeEntities = this.createBoolean("shadeEntities", data.shadeEntities(), b -> this.getOrCreateCustom().shadeEntities = b);

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
            this.preset,
            p -> {
                this.preset = p;
                this.presetChanged(list, shadeBlocks, shadeClouds, shadeEntities, p);
            }
        );

        list.addHeader(QUALITY_HEADER);
        list.addSmall(this.createBoolean(
            "compatibilityMode",
            this.compatibilityMode,
            b -> this.compatibilityMode = b
        ));

        list.addHeader(PREFERENCES_HEADER);
        list.addBig(preset);
        list.addSmall(shadeBlocks, shadeClouds);
        list.addSmall(shadeEntities);

        this.presetChanged(list, shadeBlocks, shadeClouds, shadeEntities, this.preset);
    }

    private Data getOrCreateCustom() {
        return this.custom.orElseGet(() -> {
            final var custom = new Data(this.preset.override().orElse(ConfigData.DEFAULT));
            this.custom = Optional.of(custom);
            return custom;
        });
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

        preset.override().or(() -> this.custom.map(Data::build)).ifPresent(data -> {
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

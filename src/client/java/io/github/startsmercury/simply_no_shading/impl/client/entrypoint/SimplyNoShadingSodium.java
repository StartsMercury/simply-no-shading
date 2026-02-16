package io.github.startsmercury.simply_no_shading.impl.client.entrypoint;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigPreset;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.ConfigState;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.structure.BooleanOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.StatefulOptionBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SimplyNoShadingSodium implements ConfigEntryPoint {
    private final SimplyNoShadingImpl simplyNoShading = Minecraft.getInstance().getSimplyNoShading();

    private final io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder configBuilder = Config.builder();

    private final StorageEventHandler storageHandler = () -> {
        System.out.println("TEST SIMPLY TIME TO SAVE CHANGES");
        this.simplyNoShading.setConfigAndReload(this.configBuilder.build());
    };

    private final Identifier presetOptionId = Identifier.parse("simply-no-shading:preferences.preset");

    private final Function<ConfigState, Boolean> presetEnabledProvider = config -> {
        final var preset = config.readEnumOption(this.presetOptionId, ConfigPreset.class);
        return ConfigPreset.CUSTOM.equals(preset);
    };

    @Override
    public void registerConfigLate(final ConfigBuilder builder) {
        builder
            .registerOwnModOptions()
            .setIcon(Identifier.parse("simply-no-shading:textures/gui/icon/transparent.png"))
            .addPage(builder
                .createOptionPage()
                .setName(Component.translatable("simply-no-shading.config.general.header"))
                .addOption(this.createOption(
                    builder,
                    ConfigBuilder::createBooleanOption,
                    "general.compatibility_mode",
                    "compatibilityMode",
                    io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder::setCompatibilityMode,
                    Config::compatibilityMode
                ))
            )
            .addPage(builder
                .createOptionPage()
                .setName(Component.translatable("simply-no-shading.config.preferences.header"))
                .addOptionGroup(builder.createOptionGroup().addOption(
                    this.createOption(
                        builder,
                        (builder2, id) -> builder2.createEnumOption(id, ConfigPreset.class),
                        "preferences.preset",
                        "preset",
                        io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder::setPreset,
                        Config::preset
                    ).setElementNameProvider(SnsConstants.PRESET_NAMES)
                ))
                .addOptionGroup(builder
                    .createOptionGroup()
                    .addOption(this.createShadeToggleOption(
                        builder,
                        "preferences.shade_blocks",
                        "shadeBlocks",
                        io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder::setShadeBlocks,
                        ConfigData::shadeBlocks
                    ))
                    .addOption(this.createShadeToggleOption(
                        builder,
                        "preferences.shade_clouds",
                        "shadeClouds",
                        io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder::setShadeClouds,
                        ConfigData::shadeClouds
                    ))
                    .addOption(this.createShadeToggleOption(
                        builder,
                        "preferences.shade_entities",
                        "shadeEntities",
                        io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder::setShadeEntities,
                        ConfigData::shadeEntities
                    ))
                )
            );
    }

    private BooleanOptionBuilder createShadeToggleOption(
        final ConfigBuilder builder,
        final String id,
        final String key,
        final BiConsumer<io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder, Boolean> save,
        final Function<ConfigData, Boolean> load
    ) {
        return this.createOption(
            builder,
            ConfigBuilder::createBooleanOption,
            id,
            key,
            save,
            load.compose(Config::data)
        ).setEnabledProvider(this.presetEnabledProvider, this.presetOptionId);
    }

    private <V, T extends StatefulOptionBuilder<V>> T createOption(
        final ConfigBuilder builder,
        final BiFunction<ConfigBuilder, Identifier, T> factory,
        final String id,
        final String key,
        final BiConsumer<io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigBuilder, V> save,
        final Function<Config, V> load
    ) {
        final var nameKey = "simply-no-shading.config." + key;
        final var tooltipKey = nameKey + ".tooltip";

        @SuppressWarnings("unchecked")
        final var option = (T) factory
            .apply(builder, Identifier.fromNamespaceAndPath(SnsConstants.MODID, id))
            .setName(Component.translatable(nameKey))
            .setTooltip(Component.translatable(tooltipKey))
            .setDefaultValue(load.apply(Config.DEFAULT))
            .setBinding(
                value -> save.accept(this.configBuilder, value),
                () -> load.apply(this.simplyNoShading.getConfig())
            )
            .setStorageHandler(this.storageHandler);

        return option;
    }
}

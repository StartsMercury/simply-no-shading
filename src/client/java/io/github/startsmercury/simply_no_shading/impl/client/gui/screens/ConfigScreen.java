package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.ConfigImpl;
import io.github.startsmercury.simply_no_shading.impl.client.ShadingTarget;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("simply-no-shading.config.title");
    private final ConfigImpl config;
    private OptionsList list;

    public ConfigScreen(final Screen lastScreen, final Config config) {
        super(lastScreen, Minecraft.getInstance().options, ConfigScreen.TITLE);

        Objects.requireNonNull(config, "Parameter config is null");

        this.config = new ConfigImpl(config);
    }

    @Override
    protected void init() {
        this.list = new OptionsList(super.minecraft,
            this.width,
            this.height,
            32,
            this.height - 32,
            25
        );

        final var shadingOptions = ShadingTarget
            .valueList()
            .stream()
            .map(this::createShadingOption)
            .toArray(OptionInstance[]::new);
        this.list.addSmall(shadingOptions);
        this.addWidget(this.list);
        this.addRenderableWidget(
            Button.builder(
                CommonComponents.GUI_DONE,
                button -> {
                    final var minecraft = super.minecraft;
                    assert minecraft != null;
                    minecraft.setScreen(this.lastScreen);
                }
            ).bounds(this.width / 2 - 100, this.height - 27, 200, 20)
                .build()
        );
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
    public void removed() {
        final var simplyNoShading = SimplyNoShading.instance();

        final var oldConfig = simplyNoShading.config();
        final var newConfig = this.config;
        simplyNoShading.setConfig(newConfig);
        ((SimplyNoShadingImpl) simplyNoShading).saveConfig();

        final var minecraft = super.minecraft;
        assert minecraft != null;
        SimplyNoShadingImpl.instance().applyChangesBetween(oldConfig, newConfig, minecraft);
    }

    @Override
    public void render(final PoseStack poseStack, final int i, final int j, final float f) {
        this.basicListRender(poseStack, this.list, i, j, f);
    }
}

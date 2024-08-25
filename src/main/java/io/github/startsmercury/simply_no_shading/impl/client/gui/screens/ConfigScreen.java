package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.ConfigImpl;
import io.github.startsmercury.simply_no_shading.impl.client.ReloadType;
import io.github.startsmercury.simply_no_shading.impl.client.ShadingTarget;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import java.util.Objects;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = new TranslatableComponent("simply-no-shading.config.title");
    private final ConfigImpl config;
    private OptionsList list;
    private ReloadType reloadType;

    public ConfigScreen(final Screen lastScreen, final Config config) {
        super(lastScreen, Minecraft.getInstance().options, ConfigScreen.TITLE);

        Objects.requireNonNull(config, "Parameter config is null");

        this.config = new ConfigImpl(config);
        this.reloadType = ReloadType.NONE;
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
            .toArray(Option[]::new);
        this.list.addSmall(shadingOptions);
        this.addWidget(this.list);
        this.addRenderableWidget(
            new Button(
                this.width / 2 - 100,
                this.height - 27,
                200,
                20,
                CommonComponents.GUI_DONE,
                button -> {
                    final var minecraft = super.minecraft;
                    assert minecraft != null;
                    minecraft.setScreen(this.lastScreen);
                }
            )
        );
    }

    private CycleOption<Boolean> createShadingOption(final ShadingTarget target) {
        final var key = "simply-no-shading.config.option." + target + "ShadingEnabled";
        final var tooltip = new TranslatableComponent(key + ".tooltip");
        return CycleOption.createOnOff(
            key,
            tooltip,
            options -> this.config.shadingEnabled(target),
            (options, option, enabled) -> {
                this.config.setShadingEnabled(target, enabled);
                this.reloadType = this.reloadType.compose(target.reloadType());
            }
        );
    }

    @Override
    public void removed() {
        final var simplyNoShading = SimplyNoShading.instance();

        simplyNoShading.setConfig(this.config);
        ((SimplyNoShadingImpl) simplyNoShading).saveConfig();

        final var minecraft = super.minecraft;
        assert minecraft != null;
        this.reloadType.applyTo(minecraft);
    }

    @Override
    public void render(final PoseStack poseStack, final int i, final int j, final float f) {
        this.renderBackground(poseStack);
        this.list.render(poseStack, i, j, f);
        GuiComponent.drawCenteredString(poseStack, this.font, this.title, this.width / 2, 5, 0xFFFFFF);
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, tooltipAt(this.list, i, j), i, j);
    }
}

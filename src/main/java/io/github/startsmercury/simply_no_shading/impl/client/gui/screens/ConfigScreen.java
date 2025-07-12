package io.github.startsmercury.simply_no_shading.impl.client.gui.screens;

import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.impl.client.ConfigImpl;
import io.github.startsmercury.simply_no_shading.impl.client.ShadingTarget;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.BooleanOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public final class ConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = new TranslatableComponent("simply-no-shading.config.title");
    private final ConfigImpl config;
    private OptionsList list;

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
    protected void init() {
        this.list = new OptionsList(super.minecraft,
            this.width,
            this.height,
            32,
            this.height - 32,
            25
        );

        final Option[] shadingOptions = ShadingTarget
            .valueList()
            .stream()
            .map(this::createShadingOption)
            .toArray(Option[]::new);
        this.list.addSmall(shadingOptions);
        this.children.add(this.list);
        this.addButton(
            new Button(
                this.width / 2 - 100,
                this.height - 27,
                200,
                20,
                I18n.get("gui.done"),
                button -> {
                    final Minecraft minecraft = super.minecraft;
                    assert minecraft != null;
                    minecraft.setScreen(this.lastScreen);
                }
            )
        );
    }

    private BooleanOption createShadingOption(final ShadingTarget target) {
        return new BooleanOption(
            "simply-no-shading.config.option." + target + "ShadingEnabled",
            options -> target.getFrom(config),
            (options, enabled) -> target.setInto(config, enabled)
        );
    }

    @Override
    public void removed() {
        this.configCallback.accept(this.config);
    }

    @Override
    public void render(final int i, final int j, final float f) {
        this.renderBackground();
        this.list.render(i, j, f);
        this.drawCenteredString(this.font, this.title.getColoredString(), this.width / 2, 5, 16777215);
        super.render(i, j, f);
    }
}

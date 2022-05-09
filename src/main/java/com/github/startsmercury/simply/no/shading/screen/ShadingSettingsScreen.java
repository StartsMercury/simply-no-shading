package com.github.startsmercury.simply.no.shading.screen;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

public class ShadingSettingsScreen extends OptionsSubScreen {
	private static final Option[] OPTIONS;

	private static final SimplyNoShadingClientConfig CLIENT_CONFIG;

	private static final SimplyNoShadingClientMod CLIENT_MOD;

	static {
		CLIENT_MOD = SimplyNoShadingClientMod.getInstance();

		CLIENT_CONFIG = CLIENT_MOD.config;
		OPTIONS = new Option[] {
		    CLIENT_MOD.blockShadingOption
		};
	}

	private OptionsList list;

	private boolean shouldHaveShade;

	private boolean wouldHaveShadeBlocks;

	public ShadingSettingsScreen(final Screen screen) {
		super(screen, null, new TranslatableComponent("simply-no-shading.options.shadingTitle"));
	}

	@Override
	protected void init() {
		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.shouldHaveShade = CLIENT_CONFIG.shouldShade();
		this.wouldHaveShadeBlocks = CLIENT_CONFIG.wouldShadeBlocks();

		this.list.addBig(CLIENT_MOD.shadingOption);
		this.list.addSmall(OPTIONS);

		addWidget(this.list);
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE,
		    button -> this.minecraft.setScreen(this.lastScreen)));
	}

	private boolean isDirty() {
		return CLIENT_CONFIG.shouldShade() != this.shouldHaveShade
		    || CLIENT_CONFIG.wouldShadeBlocks() != this.wouldHaveShadeBlocks;
	}

	@Override
	@SuppressWarnings("resource")
	public void removed() {
		CLIENT_MOD.saveConfig();

		if (!isDirty()) {
			return;
		}

		Minecraft.getInstance().levelRenderer.allChanged();
	}

	@Override
	public void render(final PoseStack poseStack, final int i, final int j, final float f) {
		renderBackground(poseStack);

		this.list.render(poseStack, i, j, f);

		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 5, 16777215);

		super.render(poseStack, i, j, f);

		final var list = tooltipAt(this.list, i, j);

		if (list != null) {
			renderTooltip(poseStack, list, i, j);
		}
	}
}

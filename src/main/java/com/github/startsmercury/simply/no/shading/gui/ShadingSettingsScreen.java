package com.github.startsmercury.simply.no.shading.gui;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingUtils.runWhenLoaded;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.ObjectCustomArrayList;
import com.mojang.blaze3d.vertex.PoseStack;

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
		OPTIONS = createOptions();
	}

	private static Option[] createOptions() {
		final var optionList = new ObjectCustomArrayList<>(3);

		optionList.add(CLIENT_MOD.blockShadingOption);
		optionList.add(CLIENT_MOD.cloudShadingOption);
		runWhenLoaded("enhancedblockentities", () -> optionList.add(CLIENT_MOD.enhancedBlockEntityShadingOption));

		return optionList.toArray(Option[]::new);
	}

	private OptionsList list;

	private boolean wouldHaveShadeBlocks;

	private boolean wouldHaveShadeClouds;

	private boolean wouldHaveShadeEnhancedBlockEntities;

	public ShadingSettingsScreen(final Screen screen) {
		super(screen, null, new TranslatableComponent("simply-no-shading.options.shadingTitle"));
	}

	private boolean allChanged() {
		return CLIENT_CONFIG.wouldShadeBlocks() != this.wouldHaveShadeBlocks
		    || CLIENT_CONFIG.wouldShadeEnhancedBlockEntities() != this.wouldHaveShadeEnhancedBlockEntities;
	}

	private boolean cloudsChanged() {
		return CLIENT_CONFIG.wouldShadeClouds() != this.wouldHaveShadeClouds;
	}

	@Override
	protected void init() {
		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.wouldHaveShadeBlocks = CLIENT_CONFIG.wouldShadeBlocks();
		this.wouldHaveShadeClouds = CLIENT_CONFIG.wouldShadeClouds();
		this.wouldHaveShadeEnhancedBlockEntities = CLIENT_CONFIG.wouldShadeEnhancedBlockEntities();

		this.list.addBig(CLIENT_MOD.allShadingOption);
		this.list.addSmall(OPTIONS);

		addWidget(this.list);
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE,
		    button -> this.minecraft.setScreen(this.lastScreen)));
	}

	@Override
	public void removed() {
		CLIENT_MOD.saveConfig();

		if (cloudsChanged() && this.minecraft.levelRenderer instanceof final CloudRenderer cloudRenderer) {
			cloudRenderer.generateClouds();
		}

		if (allChanged()) {
			this.minecraft.levelRenderer.allChanged();
		}
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

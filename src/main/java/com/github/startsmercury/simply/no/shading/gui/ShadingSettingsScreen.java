package com.github.startsmercury.simply.no.shading.gui;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingUtils.runWhenLoaded;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.ObjectCustomArrayList;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.CycleOption;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

public class ShadingSettingsScreen extends OptionsSubScreen {
	public static final CycleOption<Boolean> ALL_SHADING_OPTION;

	public static final CycleOption<Boolean> BLOCK_SHADING_OPTION;

	public static final CycleOption<Boolean> CLOUD_SHADING_OPTION;

	public static final CycleOption<Boolean> ENHANCED_BLOCK_ENTITY_SHADING_OPTION;

	private static final Option[] OPTIONS;

	static {
		ALL_SHADING_OPTION = CycleOption.createOnOff("simply-no-shading.options.allShading",
		    new TranslatableComponent("simply-no-shading.options.allShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.shouldShadeAll(),
		    (options, option, allShading) -> SimplyNoShadingClientMod.getInstance().config.setShadeAll(allShading));
		BLOCK_SHADING_OPTION = CycleOption.createOnOff("simply-no-shading.options.blockShading",
		    new TranslatableComponent("simply-no-shading.options.blockShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.shouldShadeBlocks(), (options, option,
		        blockShading) -> SimplyNoShadingClientMod.getInstance().config.setShadeBlocks(blockShading));
		CLOUD_SHADING_OPTION = CycleOption.createOnOff("simply-no-shading.options.cloudShading",
		    new TranslatableComponent("simply-no-shading.options.cloudShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.shouldShadeClouds(), (options, option,
		        cloudShading) -> SimplyNoShadingClientMod.getInstance().config.setShadeClouds(cloudShading));
		ENHANCED_BLOCK_ENTITY_SHADING_OPTION = CycleOption.createOnOff(
		    "simply-no-shading.options.enhancedblockentityShading",
		    new TranslatableComponent("simply-no-shading.options.enhancedblockentityShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.shouldShadeEnhancedBlockEntities(),
		    (options, option, enhancedblockentityShading) -> SimplyNoShadingClientMod.getInstance().config
		        .setShadeEnhancedBlockEntities(enhancedblockentityShading));

		OPTIONS = createOptions();
	}

	private static Option[] createOptions() {
		final var optionList = new ObjectCustomArrayList<>(3);

		optionList.add(BLOCK_SHADING_OPTION);
		optionList.add(CLOUD_SHADING_OPTION);
		runWhenLoaded("enhancedblockentities", () -> optionList.add(ENHANCED_BLOCK_ENTITY_SHADING_OPTION));

		return optionList.toArray(Option[]::new);
	}

	private static SimplyNoShadingClientConfig getClientConfig() {
		return getClientMod().config;
	}

	private static SimplyNoShadingClientMod getClientMod() {
		return SimplyNoShadingClientMod.getInstance();
	}

	private OptionsList list;

	private boolean wouldHaveShadeBlocks;

	private boolean wouldHaveShadeClouds;

	private boolean wouldHaveShadeEnhancedBlockEntities;

	public ShadingSettingsScreen(final Screen screen) {
		super(screen, null, new TranslatableComponent("simply-no-shading.options.shadingTitle"));
	}

	private boolean allChanged() {
		return getClientConfig().wouldShadeBlocks() != this.wouldHaveShadeBlocks
		    || getClientConfig().wouldShadeEnhancedBlockEntities() != this.wouldHaveShadeEnhancedBlockEntities;
	}

	private boolean cloudsChanged() {
		return getClientConfig().wouldShadeClouds() != this.wouldHaveShadeClouds;
	}

	@Override
	protected void init() {
		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.wouldHaveShadeBlocks = getClientConfig().wouldShadeBlocks();
		this.wouldHaveShadeClouds = getClientConfig().wouldShadeClouds();
		this.wouldHaveShadeEnhancedBlockEntities = getClientConfig().wouldShadeEnhancedBlockEntities();

		this.list.addBig(ALL_SHADING_OPTION);
		this.list.addSmall(OPTIONS);

		addWidget(this.list);
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE,
		    button -> this.minecraft.setScreen(this.lastScreen)));
	}

	@Override
	public void removed() {
		getClientMod().saveConfig();

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

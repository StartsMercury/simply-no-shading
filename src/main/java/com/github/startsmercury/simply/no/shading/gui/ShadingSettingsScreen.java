package com.github.startsmercury.simply.no.shading.gui;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.FABRIC;
import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingFabricClientMod;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.ObjectCustomArrayList;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

@Environment(CLIENT)
public class ShadingSettingsScreen extends OptionsSubScreen {
	public static final CycleOption<Boolean> ALL_SHADING_OPTION;

	public static final CycleOption<Boolean> BLOCK_SHADING_OPTION;

	public static final CycleOption<Boolean> CLOUD_SHADING_OPTION;

	public static final CycleOption<Boolean> ENHANCED_BLOCK_ENTITY_SHADING_OPTION;

	public static final CycleOption<Boolean> LIQUID_SHADING_OPTION;

	private static final Option[] OPTIONS;

	static {
		ALL_SHADING_OPTION = CycleOption.createOnOff("simply-no-shading.options.allShading",
		    new TranslatableComponent("simply-no-shading.options.allShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.allShading.shouldShade(), (options, option,
		        allShading) -> SimplyNoShadingClientMod.getInstance().config.allShading.setShade(allShading));
		BLOCK_SHADING_OPTION = CycleOption.createOnOff("simply-no-shading.options.blockShading",
		    new TranslatableComponent("simply-no-shading.options.blockShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.blockShading.shouldShade(), (options, option,
		        blockShading) -> SimplyNoShadingClientMod.getInstance().config.blockShading.setShade(blockShading));
		CLOUD_SHADING_OPTION = CycleOption.createOnOff("simply-no-shading.options.cloudShading",
		    new TranslatableComponent("simply-no-shading.options.cloudShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.cloudShading.shouldShade(), (options, option,
		        cloudShading) -> SimplyNoShadingClientMod.getInstance().config.cloudShading.setShade(cloudShading));
		ENHANCED_BLOCK_ENTITY_SHADING_OPTION = CycleOption.createOnOff(
		    "simply-no-shading.options.enhancedBlockEntityShading",
		    new TranslatableComponent("simply-no-shading.options.enhancedBlockEntityShading.tooltip"),
		    options -> FABRIC
		        ? SimplyNoShadingFabricClientMod.getInstance().config.enhancedBlockEntityShading.shouldShade()
		        : true,
		    (options, option, enhancedblockentityShading) -> {
			    if (!FABRIC) {
				    return;
			    }

			    SimplyNoShadingFabricClientMod.getInstance().config.enhancedBlockEntityShading
			        .setShade(enhancedblockentityShading);
		    });
		LIQUID_SHADING_OPTION = CycleOption.createOnOff("simply-no-shading.options.liquidShading",
		    new TranslatableComponent("simply-no-shading.options.liquidShading.tooltip"),
		    options -> SimplyNoShadingClientMod.getInstance().config.liquidShading.shouldShade(), (options, option,
		        cloudShading) -> SimplyNoShadingClientMod.getInstance().config.liquidShading.setShade(cloudShading));

		OPTIONS = createOptions();
	}

	private static Option[] createOptions() {
		final var optionList = new ObjectCustomArrayList<>(4);

		optionList.add(BLOCK_SHADING_OPTION);
		optionList.add(CLOUD_SHADING_OPTION);

		if (FABRIC && FabricLoader.getInstance().isModLoaded("enhancedblockentities")) {
			optionList.add(ENHANCED_BLOCK_ENTITY_SHADING_OPTION);
		}

		optionList.add(LIQUID_SHADING_OPTION);

		return optionList.toArray(Option[]::new);
	}

	private OptionsList list;

	private boolean wouldHaveShadeBlocks;

	private boolean wouldHaveShadeClouds;

	private boolean wouldHaveShadeEnhancedBlockEntities;

	private boolean wouldHaveShadeLiquids;

	public ShadingSettingsScreen(final Screen screen) {
		super(screen, null, new TranslatableComponent("simply-no-shading.options.shadingTitle"));
	}

	private boolean blockShadingChanged() {
		return SimplyNoShadingClientMod.getInstance().config.blockShading.wouldShade() != this.wouldHaveShadeBlocks;
	}

	private boolean cloudShadingChanged() {
		return SimplyNoShadingClientMod.getInstance().config.cloudShading.shouldShade() != this.wouldHaveShadeClouds;
	}

	private boolean enhancedBlockEntityShadingChanged() {
		return FABRIC && SimplyNoShadingFabricClientMod.getInstance().config.enhancedBlockEntityShading
		    .wouldShade() != this.wouldHaveShadeEnhancedBlockEntities;
	}

	@Override
	protected void init() {
		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.wouldHaveShadeBlocks = SimplyNoShadingClientMod.getInstance().config.blockShading.shouldShade();
		this.wouldHaveShadeClouds = SimplyNoShadingClientMod.getInstance().config.cloudShading.shouldShade();
		this.wouldHaveShadeEnhancedBlockEntities = FABRIC
		    && SimplyNoShadingFabricClientMod.getInstance().config.blockShading.shouldShade();

		this.list.addBig(ALL_SHADING_OPTION);
		this.list.addSmall(OPTIONS);

		addWidget(this.list);
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE,
		    button -> this.minecraft.setScreen(this.lastScreen)));
	}

	private boolean liquidShadingChanged() {
		return SimplyNoShadingClientMod.getInstance().config.liquidShading.wouldShade() != this.wouldHaveShadeLiquids;
	}

	@Override
	public void removed() {
		SimplyNoShadingClientMod.getInstance().saveConfig();

		if (cloudShadingChanged()) {
			((CloudRenderer) this.minecraft.levelRenderer).generateClouds();
		}

		if (blockShadingChanged() || enhancedBlockEntityShadingChanged() || liquidShadingChanged()) {
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

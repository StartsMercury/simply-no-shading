package com.github.startsmercury.simply.no.shading.gui;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.FABRIC;
import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingFabricClientMod;
import com.github.startsmercury.simply.no.shading.util.ObjectCustomArrayList;
import com.mojang.blaze3d.vertex.PoseStack;

import net.coderbot.iris.Iris;
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

@Deprecated
@Environment(CLIENT)
public class DeprecatedShadingSettingsScreen extends OptionsSubScreen {
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

	private SimplyNoShadingClientConfig.Observation<?> observation;

	public DeprecatedShadingSettingsScreen(final Screen screen) {
		super(screen, null, new TranslatableComponent("simply-no-shading.options.shadingTitle"));
	}

	@Override
	protected void init() {
		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.observation = SimplyNoShadingClientMod.getInstance().config.observe();

		this.list.addBig(ALL_SHADING_OPTION);
		this.list.addSmall(OPTIONS);

		addWidget(this.list);
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE,
		    button -> this.minecraft.setScreen(this.lastScreen)));
	}

	@Override
	public void removed() {
		SimplyNoShadingClientMod.getInstance().saveConfig();

		if (FabricLoader.getInstance().isModLoaded("iris") && Iris.getIrisConfig().areShadersEnabled()) {
			return;
		}

		this.observation.consume(this.minecraft);
	}

	@Override
	public void render(final PoseStack poseStack, final int mouseX, final int mouseY, final float delta) {
		renderBackground(poseStack);

		this.list.render(poseStack, mouseX, mouseY, delta);

		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 5, 16777215);

		super.render(poseStack, mouseX, mouseY, delta);

		final var list = tooltipAt(this.list, mouseX, mouseY);

		if (list != null) {
			renderTooltip(poseStack, list, mouseX, mouseY);
		}
	}
}

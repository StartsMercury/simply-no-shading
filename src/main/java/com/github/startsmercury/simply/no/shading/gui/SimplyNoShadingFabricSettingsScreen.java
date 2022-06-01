package com.github.startsmercury.simply.no.shading.gui;

import static com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod.LOGGER;

import java.util.Iterator;
import java.util.Map.Entry;

import com.github.startsmercury.simply.no.shading.config.ShadingRule;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig.Observation;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.background.DirtTexturedBackground;
import dev.lambdaurora.spruceui.option.SpruceBooleanOption;
import dev.lambdaurora.spruceui.option.SpruceSeparatorOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.util.RenderUtil;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

public class SimplyNoShadingFabricSettingsScreen extends SpruceScreen {
	protected static SpruceBooleanOption createOption(final String name, final ShadingRule shadingRule) {
		return new SpruceBooleanOption("simply-no-shading.option.shadingRule." + name, shadingRule::shouldShade,
		    shadingRule::setShade,
		    new TranslatableComponent("simply-no-shading.option.shadingRule." + name + ".tooltip"));
	}

	protected final SimplyNoShadingClientConfig<?> config;

	protected Observation<?> observation;

	protected SpruceOptionListWidget optionsWidget;

	protected final Screen parent;

	public SimplyNoShadingFabricSettingsScreen(final Screen parent) {
		this(parent, SimplyNoShadingClientMod.getInstance().config);
	}

	public SimplyNoShadingFabricSettingsScreen(final Screen parent, final SimplyNoShadingClientConfig<?> config) {
		super(new TranslatableComponent("simply-no-shading.options.title"));

		this.config = config;
		this.parent = parent;
	}

	protected void addAdvanceOptions() {
		this.optionsWidget
		    .addSingleOptionEntry(new SpruceSeparatorOption("simply-no-shading.option.advance", true, null));

		final var smartReloadOption = new SpruceBooleanOption("simply-no-shading.option.smartReload",
		    this.config::isSmartReload, this.config::setSmartReload,
		    new TranslatableComponent("simply-no-shading.option.smartReload.tooltip"));
		final var smartReloadMessageOption = new SpruceBooleanOption("simply-no-shading.option.smartReloadMessage",
		    this.config::isSmartReloadMessage, this.config::setSmartReloadMessage,
		    new TranslatableComponent("simply-no-shading.option.smartReloadMessage.tooltip"));
		this.optionsWidget.addOptionEntry(smartReloadOption, smartReloadMessageOption);
	}

	protected void addOptions() {
		addShadingOptions();
		addAdvanceOptions();
	}

	protected void addShadingOptions() {
		final var iterator = this.config.shadingRules.iterator();

		this.optionsWidget
		    .addSingleOptionEntry(new SpruceSeparatorOption("simply-no-shading.option.shadingRules", true, null));

		while (iterator.hasNext()) {
			final var leftEntry = nextOption(iterator);

			if (leftEntry == null) {
				continue;
			}

			final var rightEntry = nextOption(iterator);

			final var leftOption = createOption(leftEntry.getKey(), leftEntry.getValue());
			final var rightOption = rightEntry != null ? createOption(rightEntry.getKey(), rightEntry.getValue())
			    : null;

			this.optionsWidget.addOptionEntry(leftOption, rightOption);
		}
	}

	protected boolean applyOption(final String name) {
		return switch (name) {
		case "all" -> false;
		case "enhancedBlockEntities" -> FabricLoader.getInstance().isModLoaded("enhancedblockentities");
		default -> true;
		};
	}

	@Override
	protected void init() {
		super.init();

		final var isLevel = this.minecraft.level != null;

		this.observation = this.config.observe();
		this.optionsWidget = new SpruceOptionListWidget(Position.of(0, 34), this.width, this.height - 69);
		this.optionsWidget.setBackground(new DirtTexturedBackground(32, 32, 32, isLevel ? 0 : 255));
		addOptions();

		addRenderableWidget(this.optionsWidget);
		addRenderableWidget(new SpruceButtonWidget(Position.of(this.width / 2 - 100, this.height - 27), 200, 20,
		    CommonComponents.GUI_DONE, button -> onClose()));
	}

	private Entry<String, ShadingRule> nextOption(final Iterator<Entry<String, ShadingRule>> iterator) {
		while (iterator.hasNext()) {
			final var entry = iterator.next();

			if (applyOption(entry.getKey())) {
				return entry;
			}
		}

		return null;
	}

	@Override
	public void onClose() {
		LOGGER.debug("Closing settings screen...");

		super.onClose();

		LOGGER.info("Closed settings screen");
	}

	@Override
	public void removed() {
		LOGGER.debug("Removing settings screen...");

		SimplyNoShadingClientMod.getInstance().saveConfig();

		if (!FabricLoader.getInstance().isModLoaded("iris") || !Iris.getIrisConfig().areShadersEnabled()) {
			this.observation.react(this.minecraft);
		}

		LOGGER.info("Removed settings screen");
	}

	@Override
	public void render(final PoseStack poseStack, final int mouseX, final int mouseY, final float delta) {
		if (this.minecraft.level != null) {
			this.fillGradient(poseStack, 0, 0, this.width, this.height, 0x4F232323, 0x4F232323);
			RenderUtil.renderBackgroundTexture(0, 0, this.width, 34, 0);
			RenderUtil.renderBackgroundTexture(0, this.height - 35, this.width, 35, 0);
		}

		super.render(poseStack, mouseX, mouseY, delta);

		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 5, 0xFFFFFF);
	}

	@Override
	public void renderBackground(final PoseStack poseStack) {
		if (this.minecraft.level == null) {
			super.renderBackground(poseStack);
		}
	}
}

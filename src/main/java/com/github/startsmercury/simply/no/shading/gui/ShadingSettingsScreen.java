package com.github.startsmercury.simply.no.shading.gui;

import static com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod.LOGGER;
import static net.fabricmc.api.EnvType.CLIENT;

import java.util.Iterator;
import java.util.Map.Entry;

import com.github.startsmercury.simply.no.shading.config.ShadingRule;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.mojang.blaze3d.vertex.PoseStack;

import net.coderbot.iris.Iris;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.CycleOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

@Environment(CLIENT)
public class ShadingSettingsScreen extends OptionsSubScreen {
	protected static CycleOption<Boolean> createOption(final String name, final ShadingRule shadingRule) {
		return CycleOption.createOnOff("simply-no-shading.options.shadingRule." + name,
		    new TranslatableComponent("simply-no-shading.options.shadingRule." + name + ".tooltip"),
		    options -> shadingRule.shouldShade(), (options, option, allShading) -> shadingRule.setShade(allShading));
	}

	private final SimplyNoShadingClientConfig<?> config;

	private OptionsList list;

	private SimplyNoShadingClientConfig.Observation<?> observation;

	public ShadingSettingsScreen(final Screen screen) {
		this(screen, SimplyNoShadingClientMod.getInstance().config);
	}

	public ShadingSettingsScreen(final Screen screen, final SimplyNoShadingClientConfig<?> config) {
		super(screen, null, new TranslatableComponent("simply-no-shading.options.shadingTitle"));

		this.config = config;
	}

	protected void addOptions() {
		final var iterator = this.config.shadingRules.iterator();

		while (iterator.hasNext()) {
			final var leftEntry = nextOption(iterator);

			if (leftEntry == null) {
				continue;
			}

			final var rightEntry = nextOption(iterator);

			final var leftOption = createOption(leftEntry.getKey(), leftEntry.getValue());
			final var rightOption = rightEntry != null ? createOption(rightEntry.getKey(), rightEntry.getValue())
			    : null;

			this.list.addSmall(leftOption, rightOption);
		}
	}

	protected boolean applyOption(final String name) {
		return switch (name) {
		case "all" -> false;
		default -> true;
		};
	}

	@Override
	protected void init() {
		LOGGER.debug("Initializing settings screen...");

		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.observation = this.config.observe();

		this.list.addBig(createOption("all", this.config.shadingRules.all));

		addOptions();

		addWidget(this.list);
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE,
		    button -> this.minecraft.setScreen(this.lastScreen)));

		LOGGER.debug("Initialized settings screen");
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

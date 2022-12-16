package com.github.startsmercury.simply.no.shading.gui;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.LOGGER;
import static net.fabricmc.api.EnvType.CLIENT;
import static net.minecraft.client.OptionInstance.cachedConstantTooltip;

import java.util.Iterator;
import java.util.Map.Entry;

import com.github.startsmercury.simply.no.shading.config.ShadingRule;
import com.github.startsmercury.simply.no.shading.config.ShadingRules;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.mojang.blaze3d.vertex.PoseStack;

import net.coderbot.iris.Iris;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * The shading settings screen covering the {@link ShadingRules shading rules}
 * in the {@link SimplyNoShadingClientConfig config}.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class ShadingSettingsScreen extends OptionsSubScreen {
	/**
	 * Creates a new option given the name, and the {@link ShadingRule shading rule}
	 * from a predefined template.
	 *
	 * @param name        the name
	 * @param shadingRule the shading rule
	 * @return a new shading option
	 */
	protected static OptionInstance<Boolean> createOption(final String name, final ShadingRule shadingRule) {
		return OptionInstance.createBoolean("simply-no-shading.option.shadingRule." + name,
		        cachedConstantTooltip(
		                Component.translatable("simply-no-shading.option.shadingRule." + name + ".tooltip")),
		        shadingRule.shouldShade(), shadingRule::setShade);
	}

	/**
	 * The config.
	 */
	private final SimplyNoShadingClientConfig<?> config;

	/**
	 * The list containing the options.
	 */
	private OptionsList list;

	/**
	 * The observed changes to the config.
	 */
	private SimplyNoShadingClientConfig.Observation<?> observation;

	/**
	 * Creates a new ShadingSettingsScreen given the parent screen.
	 *
	 * @param parent the parent screen
	 */
	public ShadingSettingsScreen(final Screen parent) {
		this(parent, SimplyNoShadingClientMod.getInstance().config);
	}

	/**
	 * Creates a new ShadingSettingsScreen given the parent screen and the config.
	 *
	 * @param parent the parent screen
	 * @param config the config
	 */
	public ShadingSettingsScreen(final Screen parent, final SimplyNoShadingClientConfig<?> config) {
		super(parent, null, Component.translatable("simply-no-shading.options.shadingTitle"));

		this.config = config;
	}

	/**
	 * Adds all the options.
	 */
	protected void addOptions() {
		final var iterator = this.config.shadingRules.iterator();

		while (iterator.hasNext()) {
			final var leftEntry = nextOption(iterator);

			if (leftEntry == null)
				continue;

			final var rightEntry = nextOption(iterator);

			final var leftOption = createOption(leftEntry.getKey(), leftEntry.getValue());
			final var rightOption = rightEntry != null ? createOption(rightEntry.getKey(), rightEntry.getValue())
			        : null;

			this.list.addSmall(leftOption, rightOption);
		}
	}

	/**
	 * Filters out which options with a given name are applied.
	 *
	 * @param name the option name
	 * @return a {@code boolean} value
	 */
	protected boolean applyOption(final String name) {
		return switch (name) {
		case "all" -> false;
		default -> true;
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init() {
		LOGGER.debug("Initializing settings screen...");

		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.observation = this.config.observe();

		this.list.addBig(createOption("all", this.config.shadingRules.all));

		addOptions();

		addWidget(this.list);
		addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.lastScreen))
		        .bounds(this.width / 2 - 100, this.height - 27, 200, 20).build());
		// addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE,
		//        button -> this.minecraft.setScreen(this.lastScreen), null));

		LOGGER.debug("Initialized settings screen");
	}

	/**
	 * Searches for the next applicable option, returns {@code null} if there is
	 * none.
	 *
	 * @param iterator the name to rule entry iterator
	 * @return the next application option
	 * @see #applyOption(String)
	 */
	private Entry<String, ShadingRule> nextOption(final Iterator<Entry<String, ShadingRule>> iterator) {
		while (iterator.hasNext()) {
			final var entry = iterator.next();

			if (applyOption(entry.getKey()))
				return entry;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose() {
		LOGGER.debug("Closing settings screen...");

		super.onClose();

		LOGGER.info("Closed settings screen");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removed() {
		LOGGER.debug("Removing settings screen...");

		SimplyNoShadingClientMod.getInstance().saveConfig();

		if (!FabricLoader.getInstance().isModLoaded("iris") || !Iris.getIrisConfig().areShadersEnabled())
			this.observation.react(this.minecraft);

		LOGGER.info("Removed settings screen");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(final PoseStack poseStack, final int mouseX, final int mouseY, final float delta) {
		renderBackground(poseStack);

		this.list.render(poseStack, mouseX, mouseY, delta);

		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 5, 16777215);

		super.render(poseStack, mouseX, mouseY, delta);

		// final var list = tooltipAt(this.list, mouseX, mouseY);

		// if (list != null)
		//	renderTooltip(poseStack, list, mouseX, mouseY);
	}
}

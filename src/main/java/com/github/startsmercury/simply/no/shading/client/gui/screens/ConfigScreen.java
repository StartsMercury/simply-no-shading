package com.github.startsmercury.simply.no.shading.client.gui.screens;

import static com.github.startsmercury.simply.no.shading.client.SimplyNoShading.LOGGER;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.minecraft.client.BooleanOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * The {@code ConfigScreen} class is an implementation of {@link Screen}
 * that functions as the config screen or Simpl No Shading.
 * <p>
 * Like any other screens for minecraft, it can be displayed by using
 * {@link Minecraft#setScreen(Screen)}.
 *
 * @since 6.0.0
 */
public class ConfigScreen extends Screen {
	/**
	 * The height of the button panel of this screen.
	 */
	private static final int BUTTON_PANEL_HEIGHT = 35;

	/**
	 * The default title for Simply No Shading's config screen.
	 */
	public static final Component DEFAULT_TITLE = new TranslatableComponent("simply-no-shading.config.title");

	/**
	 * The color used over panorama and level renders.
	 */
	@SuppressWarnings("unused")
	private static final int RENDER_GRADIENT_COLOR = 0x4F141414;

	/**
	 * The height of the title panel of this screen.
	 */
	private static final int TITLE_PANEL_HEIGHT = 34;

	/**
	 * The config builder to build immutable config objects.
	 */
	protected Config.Builder configBuilder;

	/**
	 * The options widget that contains the options for the user to interact.
	 */
	protected OptionsList optionsWidget;

	/**
	 * The panorama renderer used when there is no level to render.
	 */
	protected final PanoramaRenderer panoramaRenderer;

	/**
	 * The parent screen who'll regain display once this screen is done for :).
	 */
	protected final Screen parent;

	/**
	 * Creates a new screen with a set parent, {@linkplain #DEFAULT_TITLE default
	 * title}, and default config builder.
	 *
	 * @param parent the parent screen
	 */
	public ConfigScreen(final Screen parent) {
		this(parent, DEFAULT_TITLE);
	}

	/**
	 * Creates a new screen with a set parent, title, and default config builder.
	 *
	 * @param parent the parent screen
	 * @param title  the screen title
	 */
	protected ConfigScreen(final Screen parent, final Component title) {
		this(parent, title, Config.builder(SimplyNoShading.getFirstInstance().getConfig()));
	}

	/**
	 * Creates a new screen with a set parent, title, and config builder.
	 *
	 * @param parent        the parent screen
	 * @param title         the screen title
	 * @param configBuilder the congfig builder
	 */
	protected ConfigScreen(final Screen parent, final Component title, final Config.Builder configBuilder) {
		super(title);

		this.panoramaRenderer = new PanoramaRenderer(TitleScreen.CUBE_MAP);
		this.parent = parent;
		this.configBuilder = configBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init() {
		super.init();

		final BooleanOption blockShadingEnabledOption;
		final BooleanOption cloudShadingEnabledOption;
		final BooleanOption entityShadingEnabledOption;
		final Button doneButton;

		this.optionsWidget = new OptionsList(this.minecraft,
		        this.width,
		        this.height,
		        TITLE_PANEL_HEIGHT,
		        this.height - TITLE_PANEL_HEIGHT - BUTTON_PANEL_HEIGHT,
		        25);
		blockShadingEnabledOption = new BooleanOption("simply-no-shading.config.option.blockShadingEnabled",
		        options -> this.configBuilder.isBlockShadingEnabled(),
		        (options, enabled) -> this.configBuilder.setBlockShadingEnabled(enabled));
		cloudShadingEnabledOption = new BooleanOption("simply-no-shading.config.option.cloudShadingEnabled",
		        options -> this.configBuilder.isCloudShadingEnabled(),
		        (options, enabled) -> this.configBuilder.setCloudShadingEnabled(enabled));
		entityShadingEnabledOption = new BooleanOption("simply-no-shading.config.option.entityShadingEnabled",
		        options -> this.configBuilder.isEntityShadingEnabled(),
		        (options, enabled) -> this.configBuilder.setEntityShadingEnabled(enabled));
		{
			final int buttonWidth = 200;
			final int buttonHeight = 20;
			doneButton = new Button((this.width - buttonWidth) / 2,
			        this.height - buttonHeight - 7,
			        buttonWidth,
			        buttonHeight,
			        I18n.get("gui.done"),
			        button -> onClose());
		}

		this.optionsWidget.addSmall(blockShadingEnabledOption, cloudShadingEnabledOption);
		this.optionsWidget.addBig(entityShadingEnabledOption);

		this.children.add(this.optionsWidget);
		addButton(doneButton);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removed() {
		if (this.configBuilder != null)
			SimplyNoShading.getFirstInstance().setConfig(this.configBuilder.build());
		else
			LOGGER.warn("[Simply No Shading] " + this + " tried to save changes from a null config builder");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(final int mouseX, final int mouseY, final float delta) {
		this.renderBackground();
		this.optionsWidget.render(mouseX, mouseY, delta);
		super.render(mouseX, mouseY, delta);

		final int titlePosX = this.width / 2;
		final int titlePosY = 14;
		drawCenteredString(this.font, this.title.getColoredString(), titlePosX, titlePosY, 0xFFFFFFFF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderBackground(final int z) {
		super.renderBackground(z);
	}
}

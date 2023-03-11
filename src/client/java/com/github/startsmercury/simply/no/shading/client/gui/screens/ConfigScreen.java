package com.github.startsmercury.simply.no.shading.client.gui.screens;

import static com.github.startsmercury.simply.no.shading.client.SimplyNoShading.LOGGER;
import static dev.lambdaurora.spruceui.background.EmptyBackground.EMPTY_BACKGROUND;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.option.SpruceBooleanOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.util.RenderUtil;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * The {@code ConfigScreen} class is an implementation of {@link SpruceScreen}
 * that functions as the config screen or Simpl No Shading.
 * <p>
 * Like any other screens for minecraft, it can be displayed by using
 * {@link Minecraft#setScreen(Screen)}.
 *
 * @since 6.0.0
 */
public class ConfigScreen extends SpruceScreen {
	/**
	 * The default title for Simply No Shading's config screen.
	 */
	public static final Component DEFAULT_TITLE = new TranslatableComponent("simply-no-shading.config.title");

	/**
	 * The config builder to build immutable config objects.
	 */
	protected Config.Builder configBuilder;

	/**
	 * The options widget that contains the options for the user to interact.
	 */
	protected SpruceOptionListWidget optionsWidget;

	/**
	 * The panorama showed when the screen is displayed outside a level.
	 */
	private final PanoramaRenderer panorama;

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

		this.parent = parent;
		this.panorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);
		this.configBuilder = configBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init() {
		super.init();

		final SpruceBooleanOption blockShadingEnabledOption;
		final SpruceBooleanOption cloudShadingEnabledOption;
		final SpruceButtonWidget doneButton;

		this.optionsWidget = new SpruceOptionListWidget(Position.of(0, 34), this.width, this.height - 69);
		blockShadingEnabledOption = new SpruceBooleanOption("simply-no-shading.config.option.blockShadingEnabled",
		        this.configBuilder::isBlockShadingEnabled,
		        this.configBuilder::setBlockShadingEnabled,
		        new TranslatableComponent("simply-no-shading.config.option.blockShadingEnabled.tooltip"));
		cloudShadingEnabledOption = new SpruceBooleanOption("simply-no-shading.config.option.cloudShadingEnabled",
		        this.configBuilder::isCloudShadingEnabled,
		        this.configBuilder::setCloudShadingEnabled,
		        new TranslatableComponent("simply-no-shading.config.option.cloudShadingEnabled.tooltip"));
		doneButton = new SpruceButtonWidget(Position.of(this.width / 2 - 100,
		        this.height - 27), 200, 20, CommonComponents.GUI_DONE, button -> onClose());

		this.optionsWidget.setBackground(EMPTY_BACKGROUND);
		this.optionsWidget.addOptionEntry(blockShadingEnabledOption, cloudShadingEnabledOption);

		addRenderableWidget(this.optionsWidget);
		addRenderableWidget(doneButton);
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
			LOGGER.warn(this + " tried to save changes from a null config builder");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(final PoseStack poseStack, final int mouseX, final int mouseY, final float delta) {
		if (this.minecraft.level == null)
			this.panorama.render(delta, 1.0F);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		this.fillGradient(poseStack, 0, 0, this.width, this.height, 0x4F141414, 0x4F141414);

		RenderUtil.renderBackgroundTexture(0, 0, this.width, 34, 0);
		RenderUtil.renderBackgroundTexture(0, this.height - 35, this.width, 35, 0);

		super.render(poseStack, mouseX, mouseY, delta);

		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 14, 0xFFFFFF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderBackground(final PoseStack poseStack, final int vOffset) {
	}
}

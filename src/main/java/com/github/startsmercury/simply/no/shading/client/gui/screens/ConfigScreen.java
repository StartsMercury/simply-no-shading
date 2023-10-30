package com.github.startsmercury.simply.no.shading.client.gui.screens;

import static com.github.startsmercury.simply.no.shading.client.SimplyNoShading.LOGGER;
import static dev.lambdaurora.spruceui.background.EmptyBackground.EMPTY_BACKGROUND;
import static dev.lambdaurora.spruceui.util.ColorUtil.WHITE;
import static dev.lambdaurora.spruceui.util.ColorUtil.packARGBColor;
import static dev.lambdaurora.spruceui.util.RenderUtil.renderBackgroundTexture;
import static net.minecraft.network.chat.CommonComponents.GUI_DONE;

import java.util.List;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.option.SpruceBooleanOption;
import dev.lambdaurora.spruceui.option.SpruceSimpleActionOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
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
	 * The height of the button panel of this screen.
	 */
	private static final int BUTTON_PANEL_HEIGHT = 35;

	/**
	 * The default title for Simply No Shading's config screen.
	 */
	public static final Component DEFAULT_TITLE = new TranslatableComponent("simply-no-shading.config.title");

	/**
	 * Fully opaque components render completely.
	 */
	private static final float OPAQUE = 1.0F;

	/**
	 * The color used over panorama and level renders.
	 */
	private static final int RENDER_GRADIENT_COLOR = packARGBColor(20, 20, 20, 79);

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
	protected SpruceOptionListWidget optionsWidget;

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

		final SpruceBooleanOption blockShadingEnabledOption;
		final SpruceBooleanOption cloudShadingEnabledOption;
		final SpruceButtonWidget doneButton;
		final SpruceSimpleActionOption experimentalEntityLikeShadingOption;

		this.optionsWidget = new SpruceOptionListWidget(Position.of(0, TITLE_PANEL_HEIGHT),
		        this.width,
		        this.height - TITLE_PANEL_HEIGHT - BUTTON_PANEL_HEIGHT);
		blockShadingEnabledOption = new SpruceBooleanOption("simply-no-shading.config.option.blockShadingEnabled",
		        this.configBuilder::isBlockShadingEnabled,
		        this.configBuilder::setBlockShadingEnabled,
		        new TranslatableComponent("simply-no-shading.config.option.blockShadingEnabled.tooltip"));
		cloudShadingEnabledOption = new SpruceBooleanOption("simply-no-shading.config.option.cloudShadingEnabled",
		        this.configBuilder::isCloudShadingEnabled,
		        this.configBuilder::setCloudShadingEnabled,
		        new TranslatableComponent("simply-no-shading.config.option.cloudShadingEnabled.tooltip"));
		{
			final var buttonWidth = 200;
			final var buttonHeight = 20;
			final var buttonPosition = Position.of((this.width - buttonWidth) / 2, this.height - buttonHeight - 7);
			doneButton = new SpruceButtonWidget(buttonPosition,
			        buttonWidth,
			        buttonHeight,
			        GUI_DONE,
			        button -> onClose());
		}
		experimentalEntityLikeShadingOption = SpruceSimpleActionOption.of(
			"simply-no-shading.config.option.experimentalEntityLikeShading",
			button -> {
				final var packSelectionScreen = new PackSelectionScreen(
					this,
					this.minecraft.getResourcePackRepository(),
					packRepository -> {
						final var oldResourcePacks = List.copyOf(this.minecraft.options.resourcePacks);
						this.minecraft.options.resourcePacks.clear();
						this.minecraft.options.incompatibleResourcePacks.clear();
						for (final var pack : packRepository.getSelectedPacks()) {
							if (pack.isFixedPosition()) continue;
							this.minecraft.options.resourcePacks.add(pack.getId());
							if (pack.getCompatibility().isCompatible()) continue;
							this.minecraft.options.incompatibleResourcePacks.add(pack.getId());
						}
						this.minecraft.options.save();
						if (oldResourcePacks.equals(this.minecraft.options.resourcePacks)) return;
						this.minecraft.reloadResourcePacks();
					},
					this.minecraft.getResourcePackDirectory(),
					new TranslatableComponent("resourcePack.title")
				);
				minecraft.setScreen(packSelectionScreen);
			}
		);

		this.optionsWidget.addOptionEntry(blockShadingEnabledOption, cloudShadingEnabledOption);
		this.optionsWidget.addSmallSingleOptionEntry(experimentalEntityLikeShadingOption);
		this.optionsWidget.setBackground(EMPTY_BACKGROUND);

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
		if (this.minecraft.level == null) {
			this.panoramaRenderer.render(delta, OPAQUE);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
		}
		this.fillGradient(poseStack, 0, 0, this.width, this.height, RENDER_GRADIENT_COLOR, RENDER_GRADIENT_COLOR);

		renderBackgroundTexture(0, 0, this.width, TITLE_PANEL_HEIGHT, 0);
		renderBackgroundTexture(0, this.height - BUTTON_PANEL_HEIGHT, this.width, BUTTON_PANEL_HEIGHT, 0);

		super.render(poseStack, mouseX, mouseY, delta);

		final var titlePosX = this.width / 2;
		final var titlePosY = 14;
		drawCenteredString(poseStack, this.font, this.title, titlePosX, titlePosY, WHITE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderBackground(final PoseStack poseStack, final int z) {
		// Override fogging that would have been applied to the panels' background
	}
}

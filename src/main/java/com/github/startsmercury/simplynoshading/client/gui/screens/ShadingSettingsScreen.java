package com.github.startsmercury.simplynoshading.client.gui.screens;

import static com.github.startsmercury.simplynoshading.client.SimplyNoShadingOption.SHADE_ALL;
import static com.github.startsmercury.simplynoshading.client.SimplyNoShadingOption.SHADE_BLOCKS;
import static com.github.startsmercury.simplynoshading.client.SimplyNoShadingOption.SHADE_CLOUDS;
import static com.github.startsmercury.simplynoshading.client.SimplyNoShadingOption.SHADE_FLUIDS;
import static net.minecraft.network.chat.CommonComponents.GUI_DONE;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;

public class ShadingSettingsScreen extends OptionsSubScreen {
	public static final TranslatableComponent COMPONENT;

	public static final String COMPONENT_KEY;

	private static final Option[] OPTIONS;

	static {
		COMPONENT_KEY = "simply-no-shading.options.shadingTitle";
		OPTIONS = new Option[] { SHADE_BLOCKS, SHADE_CLOUDS, SHADE_FLUIDS };
		COMPONENT = new TranslatableComponent(COMPONENT_KEY);
	}

	private OptionsList list;

	public ShadingSettingsScreen(final Screen screen, final Options options) {
		super(screen, options, COMPONENT);
	}

	@Override
	protected void init() {
		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);

		this.list.addBig(SHADE_ALL);
		this.list.addSmall(OPTIONS);

		addWidget(this.list);
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, GUI_DONE,
				button -> this.minecraft.setScreen(this.lastScreen)));
	}

	@Override
	public void removed() {
	}
	
	@Override
	public void render(final PoseStack poseStack, final int i, final int j, final float f) {
		renderBackground(poseStack);
		this.list.render(poseStack, i, j, f);
		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 5, 16777215);
		super.render(poseStack, i, j, f);
		final List<FormattedCharSequence> list = tooltipAt(this.list, i, j);
		if (list != null) {
			this.renderTooltip(poseStack, list, i, j);
		}
	}
}

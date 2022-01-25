package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus.Internal;

import net.fabricmc.api.Environment;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.TranslatableText;

@Environment(CLIENT)
@Internal
public final class SimplyNoShadingOption {
	public static final CyclingOption<Boolean> SHADE_ALL;

	public static final String SHADE_ALL_NAME_KEY;

	public static final TranslatableText SHADE_ALL_NAME;

	public static final TranslatableText SHADE_ALL_TOOLTIP;

	public static final TranslatableText SHADE_ANY_TOOLTIP;

	public static final CyclingOption<Boolean> SHADE_BLOCKS;

	public static final String SHADE_BLOCKS_NAME_KEY;

	public static final TranslatableText SHADE_BLOCKS_NAME;

	public static final CyclingOption<Boolean> SHADE_FLUIDS;

	public static final String SHADE_FLUIDS_NAME_KEY;

	public static final TranslatableText SHADE_FLUIDS_NAME;

	static {
		SHADE_ALL_NAME_KEY = "simply-no-shading.options.shade_all";
		SHADE_ALL_TOOLTIP = new TranslatableText("simply-no-shading.options.shade_all.tooltip");
		SHADE_ANY_TOOLTIP = new TranslatableText("simply-no-shading.options.shade_any.tooltip");
		SHADE_BLOCKS_NAME_KEY = "simply-no-shading.options.shade_blocks";
		SHADE_FLUIDS_NAME_KEY = "simply-no-shading.options.shade_fluids";
		SHADE_ALL_NAME = new TranslatableText(SHADE_ALL_NAME_KEY);
		SHADE_BLOCKS_NAME = new TranslatableText(SHADE_BLOCKS_NAME_KEY);
		SHADE_FLUIDS_NAME = new TranslatableText(SHADE_FLUIDS_NAME_KEY);
		SHADE_ALL = CyclingOption.create(SHADE_ALL_NAME_KEY, SHADE_ALL_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeAll(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeAll(shading));
		SHADE_BLOCKS = CyclingOption.create(SHADE_BLOCKS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeBlocks(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeBlocks(shading));
		SHADE_FLUIDS = CyclingOption.create(SHADE_FLUIDS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeFluids(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeFluids(shading));
	}
}

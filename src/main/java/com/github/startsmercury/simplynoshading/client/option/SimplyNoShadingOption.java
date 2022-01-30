package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus.Internal;

import net.fabricmc.api.Environment;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.TranslatableText;

/**
 * Contains member(s) related to {@link KeyBinding}.
 */
@Environment(CLIENT)
@Internal
public final class SimplyNoShadingOption {
	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeAll
	 */
	public static final CyclingOption<Boolean> SHADE_ALL;

	/**
	 * {@link #SHADE_ALL shadeAll} name.
	 */
	public static final TranslatableText SHADE_ALL_NAME;

	/**
	 * {@link #SHADE_ALL_NAME shadeAll name} key.
	 */
	public static final String SHADE_ALL_NAME_KEY;

	/**
	 * {@link #SHADE_ALL shadeAll} tooltip.
	 */
	public static final TranslatableText SHADE_ALL_TOOLTIP;

	/**
	 * {@link #SHADE_BLOCKS shadeBlocks} and {@link #SHADE_FLUIDS shadeFluids}
	 * tooltip.
	 */
	public static final TranslatableText SHADE_ANY_TOOLTIP;

	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeBlocks
	 */
	public static final CyclingOption<Boolean> SHADE_BLOCKS;

	/**
	 * {@link #SHADE_BLOCKS shadeBlocks} name.
	 */
	public static final TranslatableText SHADE_BLOCKS_NAME;

	/**
	 * {@link #SHADE_BLOCKS_NAME shadeBlocks name} key.
	 */
	public static final String SHADE_BLOCKS_NAME_KEY;

	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeClouds
	 */
	public static final CyclingOption<Boolean> SHADE_CLOUDS;

	/**
	 * {@link #SHADE_CLOUDS shadeClouds} name.
	 */
	public static final TranslatableText SHADE_CLOUDS_NAME;

	/**
	 * {@link #SHADE_CLOUDS_NAME shadeClouds name} key.
	 */
	public static final String SHADE_CLOUDS_NAME_KEY;

	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeFluids
	 */
	public static final CyclingOption<Boolean> SHADE_FLUIDS;

	/**
	 * {@link #SHADE_FLUIDS shadeFluids} name.
	 */
	public static final TranslatableText SHADE_FLUIDS_NAME;

	/**
	 * {@link #SHADE_FLUIDS_NAME shadeFluids name} key.
	 */
	public static final String SHADE_FLUIDS_NAME_KEY;

	static {
		SHADE_ALL_NAME_KEY = "simply-no-shading.options.shade_all";
		SHADE_ALL_TOOLTIP = new TranslatableText("simply-no-shading.options.shade_all.tooltip");
		SHADE_ANY_TOOLTIP = new TranslatableText("simply-no-shading.options.shade_any.tooltip");
		SHADE_BLOCKS_NAME_KEY = "simply-no-shading.options.shade_blocks";
		SHADE_CLOUDS_NAME_KEY = "simply-no-shading.options.shade_clouds";
		SHADE_FLUIDS_NAME_KEY = "simply-no-shading.options.shade_fluids";
		SHADE_ALL_NAME = new TranslatableText(SHADE_ALL_NAME_KEY);
		SHADE_BLOCKS_NAME = new TranslatableText(SHADE_BLOCKS_NAME_KEY);
		SHADE_CLOUDS_NAME = new TranslatableText(SHADE_CLOUDS_NAME_KEY);
		SHADE_FLUIDS_NAME = new TranslatableText(SHADE_FLUIDS_NAME_KEY);
		SHADE_ALL = CyclingOption.create(SHADE_ALL_NAME_KEY, SHADE_ALL_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeAll(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeAll(shading));
		SHADE_BLOCKS = CyclingOption.create(SHADE_BLOCKS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeBlocks(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeBlocks(shading));
		SHADE_CLOUDS = CyclingOption.create(SHADE_CLOUDS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeClouds(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeClouds(shading));
		SHADE_FLUIDS = CyclingOption.create(SHADE_FLUIDS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeFluids(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeFluids(shading));
	}
}

package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;

import net.minecraft.client.CycleOption;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.fabricmc.api.Environment;

/**
 * Contains member(s) related to {@link net.minecraft.client.KeyMapping}.
 */
@Environment(CLIENT)
@Internal
public final class SimplyNoShadingOption {
	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeAll
	 */
	public static final CycleOption<Boolean> SHADE_ALL;

	/**
	 * {@link #SHADE_ALL shadeAll} name.
	 */
	public static final TranslatableComponent SHADE_ALL_NAME;

	/**
	 * {@link #SHADE_ALL_NAME shadeAll name} key.
	 */
	public static final String SHADE_ALL_NAME_KEY;

	/**
	 * {@link #SHADE_ALL shadeAll} tooltip.
	 */
	public static final TranslatableComponent SHADE_ALL_TOOLTIP;

	/**
	 * {@link #SHADE_BLOCKS shadeBlocks} and {@link #SHADE_FLUIDS shadeFluids}
	 * tooltip.
	 */
	public static final TranslatableComponent SHADE_ANY_TOOLTIP;

	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeBlocks
	 */
	public static final CycleOption<Boolean> SHADE_BLOCKS;

	/**
	 * {@link #SHADE_BLOCKS shadeBlocks} name.
	 */
	public static final TranslatableComponent SHADE_BLOCKS_NAME;

	/**
	 * {@link #SHADE_BLOCKS_NAME shadeBlocks name} key.
	 */
	public static final String SHADE_BLOCKS_NAME_KEY;

	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeClouds
	 */
	public static final CycleOption<Boolean> SHADE_CLOUDS;

	/**
	 * {@link #SHADE_CLOUDS shadeClouds} name.
	 */
	public static final TranslatableComponent SHADE_CLOUDS_NAME;

	/**
	 * {@link #SHADE_CLOUDS_NAME shadeClouds name} key.
	 */
	public static final String SHADE_CLOUDS_NAME_KEY;

	/**
	 * @see SimplyNoShadingGameOptions#cycleShadeFluids
	 */
	public static final CycleOption<Boolean> SHADE_FLUIDS;

	/**
	 * {@link #SHADE_FLUIDS shadeFluids} name.
	 */
	public static final TranslatableComponent SHADE_FLUIDS_NAME;

	/**
	 * {@link #SHADE_FLUIDS_NAME shadeFluids name} key.
	 */
	public static final String SHADE_FLUIDS_NAME_KEY;

	static {
		SHADE_ALL_NAME_KEY = "simply-no-shading.options.shade_all";
		SHADE_ALL_TOOLTIP = new TranslatableComponent("simply-no-shading.options.shade_all.tooltip");
		SHADE_ANY_TOOLTIP = new TranslatableComponent("simply-no-shading.options.shade_any.tooltip");
		SHADE_BLOCKS_NAME_KEY = "simply-no-shading.options.shade_blocks";
		SHADE_CLOUDS_NAME_KEY = "simply-no-shading.options.shade_clouds";
		SHADE_FLUIDS_NAME_KEY = "simply-no-shading.options.shade_fluids";
		SHADE_ALL_NAME = new TranslatableComponent(SHADE_ALL_NAME_KEY);
		SHADE_BLOCKS_NAME = new TranslatableComponent(SHADE_BLOCKS_NAME_KEY);
		SHADE_CLOUDS_NAME = new TranslatableComponent(SHADE_CLOUDS_NAME_KEY);
		SHADE_FLUIDS_NAME = new TranslatableComponent(SHADE_FLUIDS_NAME_KEY);
		SHADE_ALL = CycleOption.createOnOff(SHADE_ALL_NAME_KEY, SHADE_ALL_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeAll(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeAll(shading));
		SHADE_BLOCKS = CycleOption.createOnOff(SHADE_BLOCKS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeBlocks(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeBlocks(shading));
		SHADE_CLOUDS = CycleOption.createOnOff(SHADE_CLOUDS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeClouds(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeClouds(shading));
		SHADE_FLUIDS = CycleOption.createOnOff(SHADE_FLUIDS_NAME_KEY, SHADE_ANY_TOOLTIP,
				options -> ((SimplyNoShadingGameOptions) options).isShadeFluids(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShadeFluids(shading));
	}
}

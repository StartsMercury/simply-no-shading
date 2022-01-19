package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.TranslatableText;

@Environment(CLIENT)
public final class SimplyNoShadingOption {
	public static final CyclingOption<Boolean> SHADING;

	static {
		SHADING = CyclingOption.create("simply-no-shading.options.shading",
				new TranslatableText("simply-no-shading.options.shading.tooltip"),
				options -> ((SimplyNoShadingGameOptions) options).isShading(),
				(options, option, shading) -> ((SimplyNoShadingGameOptions) options).setShading(shading));
	}
}

package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simplynoshading.client.ButtonOption;
import com.github.startsmercury.simplynoshading.client.gui.screens.ShadingSettingsScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.VideoSettingsScreen;

/**
 * {@link Mixin mixin} for the class {@link VideoSettingsScreen}.
 */
@Mixin(VideoSettingsScreen.class)
public class VideoSettingsScreenMixin {
	/**
	 * @see net.minecraft.client.gui.components.OptionsList OptionsList
	 */
	private enum OptionsList {
		;

		/**
		 * @see net.minecraft.client.gui.components.OptionsList#addSmall(Option[])
		 *      OptionsList.addSmall(Option[])
		 */
		private static final String addSmall = "Lnet/minecraft/client/gui/components/OptionsList;addSmall([Lnet/minecraft/client/Option;)V";
	}

	/**
	 * @see VideoSettingsScreen#init()
	 */
	private static final String init = "Lnet/minecraft/client/gui/screens/VideoSettingsScreen;init()V";

	/**
	 * Adds the Shading Settings button among other options in Video Settings.
	 *
	 * @param options the first batch of options
	 * @return the new options
	 */
	@ModifyArg(method = init, at = @At(value = "INVOKE", target = OptionsList.addSmall, ordinal = 0))
	private final Option[] addShadingSettingsButton(final Option[] options) {
		final Option[] appendedOptions = new Option[options.length + 1];

		appendedOptions[0] = new ButtonOption("simply-no-shading.options.shading",
				button -> Minecraft.getInstance().setScreen(new ShadingSettingsScreen((Screen) (Object) this,
						((OptionsSubScreenAccessor) this).getOptions(), false)));

		System.arraycopy(options, 0, appendedOptions, 1, options.length);

		return appendedOptions;
	}
}

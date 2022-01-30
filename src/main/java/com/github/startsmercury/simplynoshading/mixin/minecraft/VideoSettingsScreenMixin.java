package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.startsmercury.simplynoshading.client.ButtonOption;
import com.github.startsmercury.simplynoshading.client.gui.screens.ShadingSettingsScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.VideoSettingsScreen;

/**
 * {@link Mixin mixin} for the class {@link VideoSettingsScreen}.
 */
@Mixin(VideoSettingsScreen.class)
public class VideoSettingsScreenMixin {
	/**
	 * This shadowed field allows this mixin to insert additional options.
	 */
	@Final
	@Mutable
	@Shadow
	private static Option[] OPTIONS;

	/**
	 * Adds the custom options to the video settings screen.
	 *
	 * @param callback the method callback
	 */
	@Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall([Lnet/minecraft/client/Option;)V", ordinal = 0))
	private final void addOptions(final OptionsList list, final Option[] options) {
		final Option shadingOption;

		if (list == null) {
			return;
		}

		shadingOption = new ButtonOption("simply-no-shading.options.shading",
				button -> Minecraft.getInstance().setScreen(new ShadingSettingsScreen((Screen) (Object) this,
						((OptionsSubScreenAccessor) this).getOptions())));

		if (options == null || options.length == 0) {
			list.addSmall(shadingOption, null);
		} else {
			list.addSmall(shadingOption, options[0]);

			for (int i = 1; i < options.length; i += 2) {
				list.addSmall(options[i], (i < options.length - 1) ? options[i + 1] : null);
			}
		}
	}
}

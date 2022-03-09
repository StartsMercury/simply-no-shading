package com.github.startsmercury.simplynoshading.mixin.sodium;

import static com.github.startsmercury.simplynoshading.client.gui.options.SimplyNoShadingGameOptionPages.shading;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.startsmercury.simplynoshading.client.gui.options.SimplyNoShadingGameOptionPages;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;

/**
 * {@link Mixin mixin} for the class {@link SodiumOptionsGUI}.
 */
@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsGUIMixin {
	/**
	 * This shadowed field allows this mixin to insert additional pages.
	 */
	@Final
	@Shadow(remap = false)
	private java.util.List<OptionPage> pages;

	/**
	 * Adds the custom page 'shading' to the sodium video options screen.
	 *
	 * @param callback the method callback
	 * @see SimplyNoShadingGameOptionPages#shading()
	 */
	@Inject(method = "Lme/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI;<init>(Lnet/minecraft/client/gui/screens/Screen;)V",
			at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2))
	private final void addShadingPage(final CallbackInfo callback) {
		this.pages.add(shading());
	}
}

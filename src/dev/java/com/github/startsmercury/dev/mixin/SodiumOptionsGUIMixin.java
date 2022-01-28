package com.github.startsmercury.dev.mixin;

import static com.github.startsmercury.simplynoshading.client.gui.options.SimplyNoShadingGameOptionPages.shading;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
	private List<OptionPage> pages;

	/**
	 * Adds the custom page to the sodium video options screen.
	 *
	 * @param callback the method callback
	 */
	@Inject(method = "<init>", at = @At(value = "INVOKE", shift = AFTER, target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2), remap = false)
	private final void onInitAfterInvokeAdd(final CallbackInfo callback) {
		this.pages.add(shading());
	}
}

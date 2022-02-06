package com.github.startsmercury.simplynoshading.mixin.sodium;

import static com.github.startsmercury.simplynoshading.client.gui.options.SimplyNoShadingGameOptionPages.shading;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.startsmercury.simplynoshading.client.gui.options.SimplyNoShadingGameOptionPages;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.screens.Screen;

/**
 * {@link Mixin mixin} for the class {@link SodiumOptionsGUI}.
 */
@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsGUIMixin {
	/**
	 * @see java.util.List List
	 */
	private enum List {
		;

		/**
		 * @see java.util.List#add(Object) List.add(Object)
		 */
		private static final String add = "Ljava/util/List;add(Ljava/lang/Object;)Z";
	}

	/**
	 * @see SodiumOptionsGUI#SodiumOptionsGUI(Screen)
	 */
	private static final String SodiumOptionsGUI = "Lme/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI;<init>(Lnet/minecraft/client/gui/screens/Screen;)V";

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
	@Inject(method = SodiumOptionsGUI, at = @At(value = "INVOKE", shift = AFTER, target = List.add, ordinal = 2), remap = false)
	private final void addShadingPage(final CallbackInfo callback) {
		this.pages.add(shading());
	}
}

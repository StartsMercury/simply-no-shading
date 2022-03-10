package com.github.startsmercury.simplynoshading.mixin.sodium;

import static com.github.startsmercury.simplynoshading.client.gui.options.SimplyNoShadingGameOptionPages.shading;

import java.util.List;
import java.util.ListIterator;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.startsmercury.simplynoshading.client.gui.options.SimplyNoShadingGameOptionPages;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.network.chat.TranslatableComponent;

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
	 * Adds the custom page 'shading' to the sodium video options screen.
	 *
	 * @param callback the method callback
	 * @see SimplyNoShadingGameOptionPages#shading()
	 */
	@Inject(method = "Lme/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI;<init>(Lnet/minecraft/client/gui/screens/Screen;)V",
			at = @At("RETURN"))
	private final void addShadingPage(final CallbackInfo callback) {
		final ListIterator<OptionPage> pagesIterator = this.pages.listIterator();

		while (pagesIterator.hasNext()) {
			if (pagesIterator.next().getName() instanceof final TranslatableComponent translatableName &&
					"sodium.options.pages.quality".equals(translatableName.getKey())) {
				break;
			}
		}

		pagesIterator.add(shading());
	}
}

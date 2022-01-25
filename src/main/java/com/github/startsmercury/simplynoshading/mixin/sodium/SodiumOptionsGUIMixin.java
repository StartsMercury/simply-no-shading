package com.github.startsmercury.simplynoshading.mixin.sodium;

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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;

@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsGUIMixin {
	private static boolean override;

	@Final
	@Shadow(remap = false)
	private List<OptionPage> pages;

	@Final
	@Shadow(remap = false)
	private Screen prevScreen;

	@Inject(method = "<init>", at = @At(value = "INVOKE", shift = AFTER, target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2), remap = false)
	private final void onInitAfterInvokeAdd(final CallbackInfo callback) {
		this.pages.add(shading());
	}

	@Inject(method = "init", at = @At("RETURN"))
	@SuppressWarnings("resource")
	private final void onInitReturn(final CallbackInfo callback) {
		override = !override;

		if (override) {
			((SodiumOptionsGUI) (Object) this).onClose();

			MinecraftClient.getInstance()
					.setScreen(new VideoOptionsScreen(this.prevScreen, MinecraftClient.getInstance().options));
		}
	}
}

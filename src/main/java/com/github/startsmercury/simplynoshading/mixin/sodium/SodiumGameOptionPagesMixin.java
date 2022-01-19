package com.github.startsmercury.simplynoshading.mixin.sodium;

import static me.jellysquid.mods.sodium.client.gui.options.OptionFlag.REQUIRES_RENDERER_RELOAD;
import static me.jellysquid.mods.sodium.client.gui.options.OptionImpact.LOW;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;
import static org.spongepowered.asm.mixin.injection.callback.LocalCapture.CAPTURE_FAILSOFT;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.text.TranslatableText;

@Mixin(SodiumGameOptionPages.class)
public class SodiumGameOptionPagesMixin {
	@Final
	@Shadow(remap = false)
	private static MinecraftOptionsStorage vanillaOpts;

	@Inject(method = "quality", at = @At(value = "INVOKE", shift = AFTER, target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1), locals = CAPTURE_FAILSOFT, remap = false)
	private static final void onQualityReturn(final CallbackInfoReturnable<OptionPage> callback,
			final List<OptionGroup> groups) {
		// Should return 2, right?
		System.err.println(groups.size());

		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, vanillaOpts)
				.setName(new TranslatableText("simply-no-shading.options.shading"))
				.setTooltip(new TranslatableText("simply-no-shading.options.shading.tooltip")).setControl(TickBoxControl::new)
				.setBinding((options, shading) -> ((SimplyNoShadingGameOptions) options).setShading(shading),
						options -> ((SimplyNoShadingGameOptions) options).isShading())
				.setImpact(LOW).setFlags(REQUIRES_RENDERER_RELOAD).build()).build());
	}
}

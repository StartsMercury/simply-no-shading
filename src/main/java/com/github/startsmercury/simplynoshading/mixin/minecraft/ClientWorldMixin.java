package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

/**
 * {@link Mixin mixin} for the class {@link ClientWorld}.
 */
@Environment(CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	/**
	 * Adds an extra layer of check to make shading configuration consistent.
	 *
	 * @param shaded the raw shade
	 * @return the expected shade
	 * @implSpec {@code shaded && isShadeAll()}
	 */
	@ModifyVariable(method = "getBrightness", at = @At("HEAD"), argsOnly = true)
	private final boolean modifyShadedOnGetBrightness(final boolean shaded) {
		return shaded && ((SimplyNoShadingGameOptions) MinecraftClient.getInstance().options).isShadeAll();
	}
}

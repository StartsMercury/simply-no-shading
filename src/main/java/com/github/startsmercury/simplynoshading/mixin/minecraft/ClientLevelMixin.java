package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.fabricmc.api.Environment;

/**
 * {@link Mixin mixin} for the class {@link ClientLevel}.
 */
@Environment(CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin {
	/**
	 * Adds an extra layer of check to make shading configuration consistent.
	 * 
	 * @param shaded the raw shade
	 * @return the expected shade
	 * @implSpec {@code shaded && isShadeAll()}
	 */
	@ModifyVariable(method = "getShade", at = @At("HEAD"), argsOnly = true)
	private final boolean modifyShadedOnGetBrightness(final boolean shaded) {
		return shaded && ((SimplyNoShadingGameOptions) Minecraft.getInstance().options).isShadeAll();
	}
}

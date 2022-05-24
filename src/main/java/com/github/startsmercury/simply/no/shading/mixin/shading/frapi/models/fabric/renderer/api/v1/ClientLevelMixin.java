package com.github.startsmercury.simply.no.shading.mixin.shading.frapi.models.fabric.renderer.api.v1;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingFabricClientMod;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ClientLevel;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	@ModifyVariable(method = "getShade(Lnet/minecraft/core/Direction;Z)F", at = @At("HEAD"), argsOnly = true)
	private final boolean changeShaded(final boolean shaded) {
		final var shadingRules = SimplyNoShadingFabricClientMod.getInstance().config.shadingRules;

		final var wouldBlocksShade = shadingRules.blocks.wouldShade();
		final var wouldEnhancedBlockEntitiesShade = FabricLoader.getInstance().isModLoaded("enhancedblockentities")
		    && shadingRules.enhancedBlockEntities.wouldShade();

		return shaded && (wouldBlocksShade || wouldEnhancedBlockEntitiesShade);
	}
}

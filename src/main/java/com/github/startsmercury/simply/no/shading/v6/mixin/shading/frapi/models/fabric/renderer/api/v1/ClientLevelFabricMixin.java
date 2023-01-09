package com.github.startsmercury.simply.no.shading.v6.mixin.shading.frapi.models.fabric.renderer.api.v1;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.v6.entrypoint.SimplyNoShadingFabricClientMod;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ClientLevel;

/**
 * {@code ClientLevel} mixin class.
 *
 * @since 5.0.0
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelFabricMixin {
	/**
	 * Modifies the shade whenever FRAPI support is needed.
	 *
	 * @param shade the original shade
	 * @return the modified shade
	 */
	@ModifyVariable(method = "getShade(Lnet/minecraft/core/Direction;Z)F",
	                at = @At("HEAD"),
	                argsOnly = true)
	private final boolean changeShade(final boolean shade) {
		final var shadingRules = SimplyNoShadingFabricClientMod.getInstance().config.shadingRules;

		final var wouldBlocksShade = shadingRules.blocks.wouldShade();
		final var wouldEnhancedBlockEntitiesShade = FabricLoader.getInstance().isModLoaded("enhancedblockentities")
		        && shadingRules.enhancedBlockEntities.wouldShade();

		return shade && (wouldBlocksShade || wouldEnhancedBlockEntitiesShade);
	}
}

package com.github.startsmercury.simply.no.shading.mixin.shading.enhanced.block.entity.enhancedblockentities;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.startsmercury.simply.no.shading.config.FabricShadingRules;
import com.github.startsmercury.simply.no.shading.config.ShadingRule;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingFabricClientMod;
import com.github.startsmercury.simply.no.shading.impl.Shadable;

import foundationgames.enhancedblockentities.client.model.DynamicBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;

/**
 * {@code DynamicBakedModel} mixin class.
 *
 * @since 5.0.0
 */
@Mixin(DynamicBakedModel.class)
public class DynamicBakedModelFabricMixin {
	/**
	 * Modifies the {@link BakedQuad quads} to
	 * {@link Shadable#setShadingRule(ShadingRule) set the shading rule} to
	 * {@link FabricShadingRules#enhancedBlockEntities}.
	 *
	 * @param itr the iterator
	 * @return the next value in the iterator
	 */
	@Redirect(method = "emitBlockQuads",
	          at = @At(value = "INVOKE",
	                   target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
	private final Object initEmittedBlockQuads(final Iterator<?> itr) {
		final var next = itr.next();

		if (next instanceof final Shadable nextShadable)
			nextShadable.setShadingRule(
			        SimplyNoShadingFabricClientMod.getInstance().config.shadingRules.enhancedBlockEntities);

		return next;
	}
}

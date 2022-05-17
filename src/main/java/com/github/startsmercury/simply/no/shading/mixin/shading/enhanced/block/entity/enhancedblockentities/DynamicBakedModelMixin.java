package com.github.startsmercury.simply.no.shading.mixin.shading.enhanced.block.entity.enhancedblockentities;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingFabricClientMod;
import com.github.startsmercury.simply.no.shading.impl.Shadable;

import foundationgames.enhancedblockentities.client.model.DynamicBakedModel;

@Mixin(DynamicBakedModel.class)
public class DynamicBakedModelMixin {
	@Redirect(method = "emitBlockQuads",
	          at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
	private final Object initEmittedBlockQuads(final Iterator<?> var12) {
		final Object next = var12.next();

		if (next instanceof final Shadable nextShadable) {
			nextShadable.setShadingRule(SimplyNoShadingFabricClientMod.getInstance().config.enhancedBlockEntityShading);
		}

		return next;
	}
}

package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig.ShadingRule;
import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simply.no.shading.impl.Shadable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin implements Shadable {
	private ShadingRule shadingRule = SimplyNoShadingClientMod.getInstance().config.blockShading;

	@Inject(method = "isShade()Z", at = @At("RETURN"), cancellable = true)
	private final void changeReturnedShade(final CallbackInfoReturnable<Boolean> callback) {
		callback.setReturnValue(this.shadingRule.wouldShade(callback.getReturnValueZ()));
	}

	@Override
	public ShadingRule getShadingRule() {
		return this.shadingRule;
	}

	@Override
	@Shadow
	public abstract boolean isShade();

	@Override
	public void setShadingRule(final ShadingRule shadingRule) {
		this.shadingRule = shadingRule;
	}
}

package io.github.startsmercury.simply_no_shading.mixin.shading.block.bedrockify;

import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils;
import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockBlockShading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @since 6.2.0
 */
@Mixin(BedrockBlockShading.class)
@Pseudo
public class BedrockBlockShadingMixin {
    private BedrockBlockShadingMixin() {
    }

    @Inject(
        method = "getLiquidShade(Lnet/minecraft/core/Direction;Z)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private void changeReturnedShade(final CallbackInfoReturnable<Float> callback) {
        if (!SimplyNoShadingUtils.ComputedConfig.blockShadingEnabled)
            callback.setReturnValue(1.0F);
    }
}

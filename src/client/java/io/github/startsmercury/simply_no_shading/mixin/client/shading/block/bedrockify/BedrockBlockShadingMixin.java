package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.bedrockify;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockBlockShading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

/**
 * @since 6.2.0
 */
@Mixin(BedrockBlockShading.class)
@Pseudo
public class BedrockBlockShadingMixin {
    private BedrockBlockShadingMixin() {
    }

    @ModifyReturnValue(
        method = {
            "getBlockShade(Lnet/minecraft/core/Direction)F",
            "getLiquidShade(Lnet/minecraft/core/Direction;Z)F"
        },
        at = @At("HEAD")
    )
    private float modifyShade(final float original) {
        if (ComputedConfig.blockShadingEnabled) {
            return original;
        } else {
            return Math.max(1.0f, original);
        }
    }
}

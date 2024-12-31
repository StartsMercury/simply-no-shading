package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LiquidBlockRenderer.class, priority = 999)
public class LiquidBlockRendererMixin {
    private LiquidBlockRendererMixin() {
    }

    @ModifyExpressionValue(
        method = "tesselate (" +
            "Lnet/minecraft/world/level/BlockAndBiomeGetter;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lcom/mojang/blaze3d/vertex/BufferBuilder;" +
            "Lnet/minecraft/world/level/material/FluidState;" +
        ") Z",
        at = {
            @At(value = "CONSTANT", args = "floatValue=0.5", ordinal = 0),
            @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 0),
            @At(value = "CONSTANT", args = "floatValue=0.6", ordinal = 0),
            @At(value = "CONSTANT", args = "floatValue=0.5", ordinal = 1),
            @At(value = "CONSTANT", args = "floatValue=0.5", ordinal = 2),
            @At(value = "CONSTANT", args = "floatValue=0.5", ordinal = 3),
            @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 1),
            @At(value = "CONSTANT", args = "floatValue=0.6", ordinal = 1),
        }
    )
    private float changeLiquidBrightness(float constantValue) {
        if (ComputedConfig.blockShadingEnabled)
            return constantValue;
        else
            return 1.0f;
    }
}

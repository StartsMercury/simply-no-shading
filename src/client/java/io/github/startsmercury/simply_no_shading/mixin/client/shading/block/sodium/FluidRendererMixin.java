package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.sodium;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    private FluidRendererMixin() {
    }

    @ModifyVariable(
        method = """
            updateQuad(\
                Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;\
                Lnet/minecraft/world/level/BlockAndTintGetter;\
                Lnet/minecraft/core/BlockPos;\
                Lme/jellysquid/mods/sodium/client/model/light/LightPipeline;\
                Lnet/minecraft/core/Direction;\
                F\
                Lme/jellysquid/mods/sodium/client/model/quad/blender/ColorSampler;\
                Lnet/minecraft/world/level/material/FluidState;\
            )V\
        """,
        at = @At("HEAD"),
        argsOnly = true
    )
    private float changeShade(final float brightness) {
        if (ComputedConfig.blockShadingEnabled)
            return brightness;
        else
            return 1.0F;
    }
}

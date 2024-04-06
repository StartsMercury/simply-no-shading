package io.github.startsmercury.simply_no_shading.mixin.shading.block.sodium;

import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @since 6.2.0
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    private FluidRendererMixin() {
    }

    @ModifyVariable(
        method = """
            updateQuad(\
                Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;\
                Lme/jellysquid/mods/sodium/client/world/WorldSlice;\
                Lnet/minecraft/core/BlockPos;\
                Lme/jellysquid/mods/sodium/client/model/light/LightPipeline;\
                Lnet/minecraft/core/Direction;\
                F\
                Lme/jellysquid/mods/sodium/client/model/color/ColorProvider;\
                Lnet/minecraft/world/level/material/FluidState;\
            )V\
        """,
        at = @At("HEAD"),
        argsOnly = true
    )
    private float changeShade(final float brightness) {
        if (SimplyNoShadingUtils.ComputedConfig.blockShadingEnabled)
            return brightness;
        else
            return 1.0F;
    }
}

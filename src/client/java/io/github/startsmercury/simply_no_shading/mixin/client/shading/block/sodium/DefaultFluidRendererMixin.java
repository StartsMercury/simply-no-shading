package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.sodium;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DefaultFluidRenderer.class)
public class DefaultFluidRendererMixin {
    private DefaultFluidRendererMixin() {
    }

    @ModifyVariable(
        method = """
            updateQuad(\
                Lnet/caffeinemc/mods/sodium/client/model/quad/ModelQuadViewMutable;\
                Lnet/caffeinemc/mods/sodium/client/world/LevelSlice;\
                Lnet/minecraft/core/BlockPos;\
                Lnet/caffeinemc/mods/sodium/client/model/light/LightPipeline;\
                Lnet/minecraft/core/Direction;\
                Lnet/caffeinemc/mods/sodium/client/model/quad/properties/ModelQuadFacing;\
                F\
                Lnet/caffeinemc/mods/sodium/client/model/color/ColorProvider;\
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

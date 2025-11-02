package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.sodium;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(DefaultFluidRenderer.class)
public class DefaultFluidRendererMixin {
    // Instances of this class seems to be reconstructed on every rebuild. Maybe safe to bake.
    @Unique
    private final ConfigData simply_no_shading$configData = Minecraft.getInstance().getSimplyNoShading().getConfig().data();

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
        if (this.simply_no_shading$configData.shadeBlocks()) {
            return brightness;
        } else {
            return 1.0F;
        }
    }
}

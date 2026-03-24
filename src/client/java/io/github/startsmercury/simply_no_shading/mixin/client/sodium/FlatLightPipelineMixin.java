package io.github.startsmercury.simply_no_shading.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.caffeinemc.mods.sodium.client.model.light.flat.FlatLightPipeline;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FlatLightPipeline.class)
public class FlatLightPipelineMixin {
    @Final
    @Shadow
    private LightDataAccess lightCache;

    @ModifyVariable(
        method = "calculate(" +
            "Lnet/caffeinemc/mods/sodium/client/model/quad/ModelQuadView;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lnet/caffeinemc/mods/sodium/client/model/light/data/QuadLightData;" +
            "Lnet/minecraft/core/Direction;" +
            "Lnet/minecraft/core/Direction;" +
            "Z" +
            "Z" +
        ")V",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private boolean modifyShade(final boolean shade) {
        return shade && this.lightCache.getLevel().simply_no_shading$configData().shadeBlocks();
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelLighter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModelLighter.class)
public class BlockModelLighterMixin {
    @ModifyExpressionValue(
        method = "prepareQuadAmbientOcclusion(" +
            "Lnet/minecraft/client/renderer/block/BlockAndTintGetter;" +
            "Lnet/minecraft/world/level/block/state/BlockState;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lnet/minecraft/client/resources/model/geometry/BakedQuad;" +
            "Lcom/mojang/blaze3d/vertex/QuadInstance;" +
        ")V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;shade()Z"
        )
    )
    private boolean influenceCardinalLighting(
        final boolean original,
        final @Local(ordinal = 0, argsOnly = true) BlockAndTintGetter level
    ) {
        return original && level.simply_no_shading$configData().shadeBlocks();
    }
}

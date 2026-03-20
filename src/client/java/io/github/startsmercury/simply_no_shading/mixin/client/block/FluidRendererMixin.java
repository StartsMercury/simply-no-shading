package io.github.startsmercury.simply_no_shading.mixin.client.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.world.level.CardinalLighting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FluidRenderer.class, priority = 999)
public class FluidRendererMixin {
    private FluidRendererMixin() {
    }

    @WrapOperation(
        method = "tesselate(" +
            "Lnet/minecraft/client/renderer/block/BlockAndTintGetter;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lnet/minecraft/client/renderer/block/FluidRenderer$Output;" +
            "Lnet/minecraft/world/level/block/state/BlockState;" +
            "Lnet/minecraft/world/level/material/FluidState;" +
        ")V",
        at = {
            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;down()F"
            ),
            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;north()F"
            ),
            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;south()F"
            ),
            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;west()F"
            ),
            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;east()F"
            ),
        }
    )
    private float simply_no_shading$changeShade(
        final CardinalLighting cardinalLighting,
        final Operation<Float> original,
        final @Local(ordinal = 0, argsOnly = true) BlockAndTintGetter level,
        final @Share("shadeBlocks") LocalBooleanRef shadeBlocksRef
    ) {
        if (level.simply_no_shading$configData().shadeBlocks()) {
            return original.call(cardinalLighting);
        } else {
            return cardinalLighting.up();
        }
    }
}

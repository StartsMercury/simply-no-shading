package io.github.startsmercury.simply_no_shading.mixin.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonHeadRenderer.class)
public class PistonHeadRendererMixin {
    @Inject(
        method = "Lnet/minecraft/client/renderer/blockentity/PistonHeadRenderer;createMovingBlock(" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lnet/minecraft/world/level/block/state/BlockState;" +
            "Lnet/minecraft/core/Holder;" +
            "Lnet/minecraft/client/multiplayer/ClientLevel;" +
        ")Lnet/minecraft/client/renderer/block/MovingBlockRenderState;",
        at = @At("RETURN")
    )
    private static void extendRenderState(
        final CallbackInfoReturnable<MovingBlockRenderState> callback,
        final @Local(ordinal = 0, argsOnly = true) ClientLevel level
    ) {
        final var movingBlockRenderState = callback.getReturnValue();

        movingBlockRenderState.simply_no_shading$setConfigData(
            level.simply_no_shading$configData()
        );
    }
}

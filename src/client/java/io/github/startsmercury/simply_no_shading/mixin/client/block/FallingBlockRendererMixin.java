package io.github.startsmercury.simply_no_shading.mixin.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockRenderer.class)
public class FallingBlockRendererMixin {
    @Inject(
        method = "Lnet/minecraft/client/renderer/entity/FallingBlockRenderer;extractRenderState(" +
            "Lnet/minecraft/world/entity/item/FallingBlockEntity;" +
            "Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;" +
            "F" +
        ")V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/block/MovingBlockRenderState;lightEngine" +
                ":Lnet/minecraft/world/level/lighting/LevelLightEngine;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void extendRenderState(
        final CallbackInfo callback,
        final @Local(ordinal = 0, argsOnly = true) FallingBlockRenderState state,
        final @Local(ordinal = 0) ClientLevel level
    ) {
        state.movingBlockRenderState.simply_no_shading$setConfigData(
            level.simply_no_shading$configData()
        );
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.renderer.CloudRenderer;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CloudRenderer.class, priority = 1500)
public abstract class CloudRendererMixin {
    @Final
    @Shadow
    private static int FLAG_USE_TOP_COLOR;

    private CloudRendererMixin() {
    }

    @Dynamic(mixin = net.caffeinemc.mods.sodium.mixin.features.render.world.clouds.CloudRendererMixin.class)
    @Inject(
        method = "buildMesh(" +
            "Lnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;" +
            "Ljava/nio/ByteBuffer;" +
            "I" +
            "I" +
            "Z" +
            "I" +
        ")V",
        at = @At("HEAD")
    )
    private void simply_no_shading$captureShadeClouds(
        final CallbackInfo callback,
        final @Share("shadeClouds") LocalBooleanRef shadeCloudsRef
    ) {
        shadeCloudsRef.set(SimplyNoShadingImpl.renderState().shadeClouds);
    }

    @Dynamic(mixin = net.caffeinemc.mods.sodium.mixin.features.render.world.clouds.CloudRendererMixin.class)
    @ModifyExpressionValue(
        method = "buildMesh(" +
            "Lnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;" +
            "Ljava/nio/ByteBuffer;" +
            "I" +
            "I" +
            "Z" +
            "I" +
        ")V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/CloudRenderer;sodium$addCellGeometryToBuffer(JIIILnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;ZII[JII)I"
        ),
        // We need this if Sodium and its mixin is not present
        // Should consider disabling this when testing this injector
        require = 0
    )
    private int catchupBitmasks(
        final int newIndex,
        final @Local(ordinal = 0) long ptr,
        final @Local(ordinal = 5) int cellIndex,
        final @Share("shadeClouds") LocalBooleanRef shadeCloudsRef
    ) {
        if (!shadeCloudsRef.get()) {
            for (var index = cellIndex; index < newIndex; index++) {
                final var ptrIndex = ptr + index * 3L + 2L;
                final var flags = MemoryUtil.memGetByte(ptrIndex) | FLAG_USE_TOP_COLOR;
                MemoryUtil.memPutByte(ptrIndex, (byte) flags);
            }
        }
        return newIndex;
    }
}

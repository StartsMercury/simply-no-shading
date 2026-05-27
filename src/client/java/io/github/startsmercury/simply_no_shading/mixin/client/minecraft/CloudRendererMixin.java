package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    @Final
    @Shadow
    private static int FLAG_USE_TOP_COLOR;

    private CloudRendererMixin() {
    }

    @Inject(
        method = "buildExtrudedCell(" +
            "Lnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;" +
            "Ljava/nio/ByteBuffer;" +
            "I" +
            "I" +
            "J" +
        ")V",
        at = @At("HEAD")
    )
    private void simply_no_shading$captureShadeClouds(
        final CallbackInfo callback,
        final @Share("shadeClouds") LocalBooleanRef shadeCloudsRef
    ) {
        shadeCloudsRef.set(SimplyNoShadingImpl.renderState().shadeClouds);
    }

    @ModifyArg(
        method = "buildExtrudedCell(" +
            "Lnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;" +
            "Ljava/nio/ByteBuffer;" +
            "I" +
            "I" +
            "J" +
        ")V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/CloudRenderer;encodeFace(" +
                "Ljava/nio/ByteBuffer;" +
                "I" +
                "I" +
                "Lnet/minecraft/core/Direction;" +
                "I" +
            ")V"
        ),
        index = 4
    )
    private int simply_no_shading$toggleCardinalLighting(
        final int flags,
        final @Share("shadeClouds") LocalBooleanRef shadeCloudsRef
    ) {
        if (shadeCloudsRef.get()) {
            return flags;
        } else {
            return flags | CloudRendererMixin.FLAG_USE_TOP_COLOR;
        }
    }
}

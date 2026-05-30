package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    @Final
    @Shadow
    private static int FLAG_USE_TOP_COLOR;

    private CloudRendererMixin() {
    }

    @ModifyVariable(
        method = "encodeFace(Ljava/nio/ByteBuffer;IILnet/minecraft/core/Direction;I)V",
        at = @At("HEAD"),
        ordinal = 2,
        argsOnly = true
    )
    private int simply_no_shading$changeCloudBrightness(final int flags) {
        if (SimplyNoShadingImpl.mainRenderState().shadeClouds) {
            return flags;
        } else {
            return flags | CloudRendererMixin.FLAG_USE_TOP_COLOR;
        }
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    private CloudRendererMixin() {
    }

    @ModifyVariable(
        method = "encodeFace(Ljava/nio/ByteBuffer;IILnet/minecraft/core/Direction;I)V",
        at = @At("HEAD"),
        ordinal = 2,
        argsOnly = true
    )
    private int changeCloudBrightness(final int flags) {
        if (ComputedConfig.cloudShadingEnabled)
            return flags;
        else
            return flags | CloudRenderer.FLAG_USE_TOP_COLOR;
    }
}

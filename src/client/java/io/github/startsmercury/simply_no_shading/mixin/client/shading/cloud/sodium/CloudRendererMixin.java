package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.sodium;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @since 7.0.0
 */
@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    @Unique
    private int texel;

    private CloudRendererMixin() {
    }

    @ModifyVariable(
        method = "rebuildGeometry(Lcom/mojang/blaze3d/vertex/BufferBuilder;III)V",
        at = @At(value = "STORE"),
        name = "texel",
        allow = 1
    )
    private int cacheBaseColor(final int texel) {
        return this.texel = texel;
    }

    @ModifyArg(
        method = "rebuildGeometry(Lcom/mojang/blaze3d/vertex/BufferBuilder;III)V",
        at = @At(
            value = "INVOKE",
            target = """
                Lme/jellysquid/mods/sodium/client/render/immediate/CloudRenderer;writeVertex(\
                    J\
                    F\
                    F\
                    F\
                    I\
                )J\
            """
        ),
        index = 4,
        remap = false
    )
    private int undoColorMixing(final int mixedColor) {
        if (ComputedConfig.cloudShadingEnabled)
            return mixedColor;
        else
            return this.texel;
    }
}

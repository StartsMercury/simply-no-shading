package io.github.startsmercury.simply_no_shading.impl.mixin.shading.cloud.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * @since 6.2.0
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    private LevelRendererMixin() {
    }

    @ModifyConstant(
        method = """
            buildClouds(\
                Lcom/mojang/blaze3d/vertex/BufferBuilder;\
                D\
                D\
                D\
                Lnet/minecraft/world/phys/Vec3;\
            )Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;\
        """,
        constant = {
            @Constant(floatValue = 0.9f, ordinal = 0),
            @Constant(floatValue = 0.9f, ordinal = 1),
            @Constant(floatValue = 0.9f, ordinal = 2),
            @Constant(floatValue = 0.7f, ordinal = 0),
            @Constant(floatValue = 0.7f, ordinal = 1),
            @Constant(floatValue = 0.7f, ordinal = 2),
            @Constant(floatValue = 0.8f, ordinal = 0),
            @Constant(floatValue = 0.8f, ordinal = 1),
            @Constant(floatValue = 0.8f, ordinal = 2)
        }
    )
    private float changeCloudBrightness(final float constantValue) {
        if (ComputedConfig.cloudShadingEnabled)
            return constantValue;
        else
            return 1.0f;
    }
}

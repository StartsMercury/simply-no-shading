package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.minecraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    private CloudRendererMixin() {
    }

    @ModifyExpressionValue(
        method = """
            buildMesh(\
                Lcom/mojang/blaze3d/vertex/Tesselator;\
                I\
                I\
                Lnet/minecraft/client/CloudStatus;\
                Lnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;\
                Lnet/minecraft/client/renderer/RenderType;\
            )Lcom/mojang/blaze3d/vertex/MeshData;\
        """,
        at = {
            @At(value = "CONSTANT", args = "floatValue=0.9", ordinal = 0),
            @At(value = "CONSTANT", args = "floatValue=0.9", ordinal = 1),
            @At(value = "CONSTANT", args = "floatValue=0.9", ordinal = 2),
            @At(value = "CONSTANT", args = "floatValue=0.7", ordinal = 0),
            @At(value = "CONSTANT", args = "floatValue=0.7", ordinal = 1),
            @At(value = "CONSTANT", args = "floatValue=0.7", ordinal = 2),
            @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 5),
            @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 6),
            @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 7),
        }
    )
    private float changeCloudBrightness(final float constantValue) {
        if (ComputedConfig.cloudShadingEnabled)
            return constantValue;
        else
            return 1.0f;
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.sodium;

import static org.objectweb.asm.Opcodes.GETSTATIC;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    @Final
    @Shadow(remap = false)
    private static int CLOUD_COLOR_POS_Y;

    private CloudRendererMixin() {
    }

    @ModifyExpressionValue(method = "*", remap = false, at = {
        @At(value = "FIELD", opcode = GETSTATIC, target = """
            Lme/jellysquid/mods/sodium/client/render/immediate/CloudRenderer;\
            CLOUD_COLOR_NEG_Y: I\
        """),
        @At(value = "FIELD", opcode = GETSTATIC, target = """
            Lme/jellysquid/mods/sodium/client/render/immediate/CloudRenderer;\
            CLOUD_COLOR_NEG_X: I\
        """),
        @At(value = "FIELD", opcode = GETSTATIC, target = """
            Lme/jellysquid/mods/sodium/client/render/immediate/CloudRenderer;\
            CLOUD_COLOR_POS_X: I\
        """),
        @At(value = "FIELD", opcode = GETSTATIC, target = """
            Lme/jellysquid/mods/sodium/client/render/immediate/CloudRenderer;\
            CLOUD_COLOR_NEG_Z: I\
        """),
        @At(value = "FIELD", opcode = GETSTATIC, target = """
            Lme/jellysquid/mods/sodium/client/render/immediate/CloudRenderer;\
            CLOUD_COLOR_POS_Z: I\
        """),
    })
    private int changeCloudColor(final int color) {
        if (ComputedConfig.cloudShadingEnabled)
            return color;
        else
            return CLOUD_COLOR_POS_Y;
    }
}

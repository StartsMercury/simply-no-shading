package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin implements SnsConfigDataAware {
    @Final
    @Shadow
    private static int FLAG_USE_TOP_COLOR;

    @Unique
    private ConfigData simply_noShading$configData = ConfigData.VANILLA;

    private CloudRendererMixin() {
    }

    @ModifyVariable(
        method = "encodeFace(Ljava/nio/ByteBuffer;IILnet/minecraft/core/Direction;I)V",
        at = @At("HEAD"),
        ordinal = 2,
        argsOnly = true
    )
    private int simply_no_shading$changeCloudBrightness(final int flags) {
        if (simply_noShading$configData.shadeClouds()) {
            return flags;
        } else {
            return flags | CloudRendererMixin.FLAG_USE_TOP_COLOR;
        }
    }

    @Override
    public ConfigData simply_no_shading$getConfigData() {
        return this.simply_noShading$configData;
    }

    @Override
    public void simply_no_shading$setConfigData(final ConfigData configData) {
        this.simply_noShading$configData = configData;
    }
}

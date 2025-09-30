package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.sodium;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Dynamic;
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

    @Dynamic(mixin = net.caffeinemc.mods.sodium.mixin.features.render.world.clouds.CloudRendererMixin.class)
    @ModifyVariable(
        method = "sodium$encodeCellFace(JJIILnet/minecraft/core/Direction;I)V",
        at = @At("HEAD"),
        ordinal = 2,
        argsOnly = true,
        require = 0
    )
    private static int changeCloudBrightness(final int extraData) {
        if (ComputedConfig.cloudShadingEnabled)
            return extraData;
        else
            return extraData | CloudRendererMixin.FLAG_USE_TOP_COLOR;
    }
}

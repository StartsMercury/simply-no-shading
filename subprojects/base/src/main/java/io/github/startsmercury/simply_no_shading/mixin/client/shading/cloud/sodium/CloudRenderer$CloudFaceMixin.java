package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.sodium;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.immediate.CloudRenderer$CloudFace")
public abstract class CloudRenderer$CloudFaceMixin {
    @Unique
    private static final int WHITE = ColorABGR.pack(1.0F, 1.0F, 1.0F, 1.0F);

    private CloudRenderer$CloudFaceMixin() {
    }

    @WrapMethod(method = "getColor()I", remap = false)
    public int changeCloudColor(final Operation<Integer> original) {
        if (ComputedConfig.cloudShadingEnabled) {
            return original.call();
        } else {
            return CloudRenderer$CloudFaceMixin.WHITE;
        }
    }
}

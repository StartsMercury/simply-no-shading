package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.caffeinemc.mods.sodium.client.render.immediate.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    private CloudRendererMixin() {
    }

    @WrapOperation(
        method = { "emitCellGeometryExterior", "emitCellGeometryInterior" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/caffeinemc/mods/sodium/api/util/ColorABGR;mulRGB(II)I"
        ),
        remap = false
    )
    private static int changeCloudColor(
        final int color,
        final int factor,
        final Operation<Integer> original
    ) {
        if (ComputedConfig.cloudShadingEnabled) {
            return original.call(color, factor);
        } else {
            return color;
        }
    }
}

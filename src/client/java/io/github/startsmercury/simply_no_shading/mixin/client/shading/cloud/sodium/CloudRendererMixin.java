package io.github.startsmercury.simply_no_shading.mixin.client.shading.cloud.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.minecraft.client.renderer.CloudRenderer;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CloudRenderer.class, priority = 1500)
public abstract class CloudRendererMixin {
    @Final
    @Shadow
    private static int FLAG_USE_TOP_COLOR;

    private CloudRendererMixin() {
    }

    @Dynamic(mixin = net.caffeinemc.mods.sodium.mixin.features.render.world.clouds.CloudRendererMixin.class)
    @ModifyExpressionValue(
        method = "buildMesh(Lnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;Ljava/nio/ByteBuffer;IIZI)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/CloudRenderer;sodium$addCellGeometryToBuffer(JIIILnet/minecraft/client/renderer/CloudRenderer$RelativeCameraPos;ZII[JII)I"
        ),
        // We need this if Sodium and its mixin is not present
        // Should consider disabling this when testing this injector
        require = 0
    )
    private int catchupBitmasks(
        final int newIndex,
        final @Local(ordinal = 0) long ptr,
        final @Local(ordinal = 5) int cellIndex
    ) {
        if (!((SnsConfigDataAware) this).simply_no_shading$getConfigData().shadeClouds()) {
            for (var index = cellIndex; index < newIndex; index++) {
                final var ptrIndex = ptr + index * 3L + 2L;
                MemoryUtil.memPutByte(ptrIndex, (byte) (MemoryUtil.memGetByte(ptrIndex) | FLAG_USE_TOP_COLOR));
            }
        }
        return newIndex;
    }
}

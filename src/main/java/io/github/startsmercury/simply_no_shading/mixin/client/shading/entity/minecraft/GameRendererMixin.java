package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @WrapMethod(method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V")
    private void modifyEntityLighting(
        final float partialTick,
        final long nanos,
        final PoseStack poseStack,
        final Operation<Void> original
    ) {
        final SimplyNoShadingImpl simplyNoShading = (SimplyNoShadingImpl) minecraft.getSimplyNoShading();
        simplyNoShading.lightingScope(() -> original.call(partialTick, nanos, poseStack));
    }

    @WrapOperation(
        method = "renderItemActivationAnimation(IIF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"
        )
    )
    private void modifyEntityLighting(
        final MultiBufferSource.BufferSource instance,
        final Operation<Void> original
    ) {
        final SimplyNoShadingImpl simplyNoShading = (SimplyNoShadingImpl) minecraft.getSimplyNoShading();
        simplyNoShading.lightingScope(() -> original.call(instance));
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @WrapMethod(method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V")
    private void modifyEntityLighting(
        final float partialTick,
        final long nanos,
        final PoseStack poseStack,
        final Operation<Void> original
    ) {
        SimplyNoShadingImpl.instance()
            .lightingScope(() -> original.call(partialTick, nanos, poseStack));
    }
}

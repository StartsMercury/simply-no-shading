package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.DeathProtectionFeature;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.ConduitRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ConduitRenderer.class)
public class ConduitRendererMixin {
    @WrapOperation(
        method = "render(Lnet/minecraft/world/level/block/entity/ConduitBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 0
        )
    )
    private VertexConsumer modifyInactiveShellRenderType(
        final Material instance,
        final MultiBufferSource multiBufferSource,
        final Function<ResourceLocation, RenderType> renderType,
        final Operation<VertexConsumer> original
    ) {
        return DeathProtectionFeature.modifyBufferRenderType(
            instance,
            multiBufferSource,
            renderType,
            original
        );
    }
}

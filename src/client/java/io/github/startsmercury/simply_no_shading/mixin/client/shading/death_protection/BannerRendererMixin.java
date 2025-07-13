package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.DeathProtectionFeature;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BannerRenderer.class)
public class BannerRendererMixin {
    @WrapOperation(
        method = "renderBanner",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    private static VertexConsumer modifyBaseRenderType(
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

    @WrapOperation(
        method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;ZZ)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;ZLnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    private static VertexConsumer modifyPatternsRenderType(
        final Material instance,
        final MultiBufferSource multiBufferSource,
        final Function<ResourceLocation, RenderType> renderType,
        final boolean bl,
        final ItemStackRenderState.FoilType foilType,
        final Operation<VertexConsumer> original
    ) {
        return DeathProtectionFeature.modifyBufferRenderType(
            instance,
            multiBufferSource,
            renderType,
            bl,
            foilType,
            original
        );
    }

    @WrapOperation(
        method = "renderPatternLayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    private static VertexConsumer modifyPatternLayerRenderType(
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

package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderPipelines;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public abstract class RenderTypeMixin {
    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/renderer/RenderType;create(Ljava/lang/String;ILcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/RenderType$CompositeState;)Lnet/minecraft/client/renderer/RenderType$CompositeRenderType;"),
        slice = @Slice(from = @At(value = "CONSTANT", ordinal = 0, args = "stringValue=translucent"))
    )
    private static RenderType.CompositeRenderType createCustomTranslucent(
        final String name,
        final int bufferSize,
        final RenderPipeline renderPipeline,
        final RenderType.CompositeState state,
        final Operation<RenderType.CompositeRenderType> original
    ) {
        final var result = original.call(name, bufferSize, renderPipeline, state);
        SnsRenderTypes.TRANSLUCENT = original.call(name + SnsConstants.LUMINOUS_SUFFIX, bufferSize, SnsRenderPipelines.TRANSLUCENT, state);
        return result;
    }

    @Inject(
        method = {
            "method_34832",
            "method_34826",
            "method_62288",
            "method_34832",
            "method_34831",
            "method_34824",
            "method_34830",
            "method_34839",
        },
        at = @At("HEAD")
    )
    private static void detectCustom(
        final CallbackInfoReturnable<RenderType> callback,
        final @Local(ordinal = 0, argsOnly = true) LocalRef<ResourceLocation> resourceLocationRef,
        final @Share("custom") LocalBooleanRef custom
    ) {
        final var resourceLocation = resourceLocationRef.get();
        if (SnsConstants.MODID.equals(resourceLocation.getNamespace())) {
            resourceLocationRef.set(
                ResourceLocation.withDefaultNamespace(resourceLocation.getPath())
            );
            custom.set(true);
        }
    }

    @ModifyExpressionValue(
        method = "method_34826",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderPipelines;ENTITY_SOLID:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    )
    private static RenderPipeline createCustomEntitySolidProvider(
        final RenderPipeline original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderPipelines.ENTITY_SOLID;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_62288",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderPipelines;ENTITY_CUTOUT:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    )
    private static RenderPipeline createCustomEntityCutoutProvider(
        final RenderPipeline original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderPipelines.ENTITY_CUTOUT;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34832",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderPipelines;ENTITY_CUTOUT_NO_CULL:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    )
    private static RenderPipeline createCustomEntityCutoutNoCullProvider(
        final RenderPipeline original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderPipelines.ENTITY_CUTOUT_NO_CULL;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34831",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderPipelines;ENTITY_CUTOUT_NO_CULL_Z_OFFSET:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    )
    private static RenderPipeline createCustomEntityCutoutNoCullZOffsetProvider(
        final RenderPipeline original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderPipelines.ENTITY_CUTOUT_NO_CULL_Z_OFFSET;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34824",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderPipelines;ITEM_ENTITY_TRANSLUCENT_CULL:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    )
    private static RenderPipeline createCustomItemEntityTranslucentCullProvider(
        final RenderPipeline original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderPipelines.ITEM_ENTITY_TRANSLUCENT_CULL;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34830",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderPipelines;ENTITY_TRANSLUCENT:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    )
    private static RenderPipeline createCustomEntityTranslucentProvider(
        final RenderPipeline original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderPipelines.ENTITY_TRANSLUCENT;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34839",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderPipelines;ENTITY_NO_OUTLINE:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    )
    private static RenderPipeline createCustomEntityNoOutlineProvider(
        final RenderPipeline original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderPipelines.ENTITY_NO_OUTLINE;
        } else {
            return original;
        }
    }
}

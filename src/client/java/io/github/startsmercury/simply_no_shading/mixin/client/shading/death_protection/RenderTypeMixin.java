package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderStateShards;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderTypes;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public class RenderTypeMixin {
    @Shadow
    private static RenderType.CompositeState translucentState(
        final RenderStateShard.ShaderStateShard shaderStateShard
    ) {
        throw new AssertionError();
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/renderer/RenderType;create(Ljava/lang/String;Lcom/mojang/blaze3d/vertex/VertexFormat;Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;IZZLnet/minecraft/client/renderer/RenderType$CompositeState;)Lnet/minecraft/client/renderer/RenderType$CompositeRenderType;"),
        slice = @Slice(from = @At(value = "CONSTANT", ordinal = 0, args = "stringValue=translucent"))
    )
    private static RenderType.CompositeRenderType createCustomTranslucent(
        final String string,
        final VertexFormat vertexFormat,
        final VertexFormat.Mode mode,
        final int i,
        final boolean bl,
        final boolean bl2,
        final RenderType.CompositeState compositeState,
        final Operation<RenderType.CompositeRenderType> original
    ) {
        final var result = original.call(string, vertexFormat, mode, i, bl, bl2, compositeState);
        SnsRenderTypes.TRANSLUCENT = original.call(string, vertexFormat, mode, i, bl, bl2, translucentState(SnsRenderStateShards.RENDERTYPE_TRANSLUCENT_SHADER));
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
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;RENDERTYPE_ENTITY_SOLID_SHADER:Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;")
    )
    private static RenderStateShard.ShaderStateShard createCustomEntitySolidProvider(
        final RenderStateShard.ShaderStateShard original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderStateShards.RENDERTYPE_ENTITY_SOLID_SHADER;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_62288",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;RENDERTYPE_ENTITY_CUTOUT_SHADER:Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;")
    )
    private static RenderStateShard.ShaderStateShard createCustomEntityCutoutProvider(
        final RenderStateShard.ShaderStateShard original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderStateShards.RENDERTYPE_ENTITY_CUTOUT_SHADER;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34832",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER:Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;")
    )
    private static RenderStateShard.ShaderStateShard createCustomEntityCutoutNoCullProvider(
        final RenderStateShard.ShaderStateShard original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderStateShards.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34831",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER:Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;")
    )
    private static RenderStateShard.ShaderStateShard createCustomEntityCutoutNoCullZOffsetProvider(
        final RenderStateShard.ShaderStateShard original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderStateShards.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34824",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER:Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;")
    )
    private static RenderStateShard.ShaderStateShard createCustomItemEntityTranslucentCullProvider(
        final RenderStateShard.ShaderStateShard original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderStateShards.RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34830",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;RENDERTYPE_ENTITY_TRANSLUCENT_SHADER:Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;")
    )
    private static RenderStateShard.ShaderStateShard createCustomEntityTranslucentProvider(
        final RenderStateShard.ShaderStateShard original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderStateShards.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "method_34839",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;RENDERTYPE_ENTITY_NO_OUTLINE_SHADER:Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;")
    )
    private static RenderStateShard.ShaderStateShard createCustomEntityNoOutlineProvider(
        final RenderStateShard.ShaderStateShard original,
        final @Share("custom") LocalBooleanRef custom
    ) {
        if (custom.get()) {
            return SnsRenderStateShards.RENDERTYPE_ENTITY_NO_OUTLINE_SHADER;
        } else {
            return original;
        }
    }
}

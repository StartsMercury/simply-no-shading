package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import static io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderPipelines.M_BUILD;
import static io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderPipelines.M_REGISTER;
import static io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderPipelines.createLuminous;
import static io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderPipelines.createNoShading;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderPipelines;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPipelines.class)
public abstract class RenderPipelinesMixin {
    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_solid"
        ))
    )
    private static RenderPipeline createCustomEntitySolid(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createLuminous(builder, original, SnsRenderPipelines::es);
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/translucent"
        ))
    )
    private static RenderPipeline createCustomTranslucent(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createNoShading(builder, original, SnsRenderPipelines::t);
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_cutout"
        ))
    )
    private static RenderPipeline createCustomEntityCutout(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createLuminous(builder, original, SnsRenderPipelines::ec);
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_cutout_no_cull"
        ))
    )
    private static RenderPipeline createCustomEntityCutoutNoCull(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createLuminous(builder, original, SnsRenderPipelines::ecnc);
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_cutout_no_cull_z_offset"
        ))
    )
    private static RenderPipeline createCustomEntityCutoutNoCullZOffset(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createLuminous(builder, original, SnsRenderPipelines::ecnczo);
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/item_entity_translucent_cull"
        ))
    )
    private static RenderPipeline createCustomItemEntityTranslucentCull(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createNoShading(builder, original, SnsRenderPipelines::ietc);
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_translucent"
        ))
    )
    private static RenderPipeline createCustomEntityTranslucent(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createLuminous(builder, original, SnsRenderPipelines::et);
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = M_BUILD, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_no_outline"
        ))
    )
    private static RenderPipeline createCustomEntityNoOutline(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original
    ) {
        return createLuminous(builder, original, SnsRenderPipelines::eno);
    }

    @Shadow
    private static RenderPipeline register(RenderPipeline renderPipeline) {
        throw new AssertionError();
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_solid"
        ))
    )
    private static void registerCustomEntitySolid(final CallbackInfo callback) {
        SnsRenderPipelines.ENTITY_SOLID = register(SnsRenderPipelines.ENTITY_SOLID);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/translucent"
        ))
    )
    private static void registerCustomTranslucent(final CallbackInfo callback) {
        SnsRenderPipelines.TRANSLUCENT = register(SnsRenderPipelines.TRANSLUCENT);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_cutout"
        ))
    )
    private static void registerCustomEntityCutout(final CallbackInfo callback) {
        SnsRenderPipelines.ENTITY_CUTOUT = register(SnsRenderPipelines.ENTITY_CUTOUT);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_cutout_no_cull"
        ))
    )
    private static void registerCustomEntityCutoutNoCull(final CallbackInfo callback) {
        SnsRenderPipelines.ENTITY_CUTOUT_NO_CULL = register(SnsRenderPipelines.ENTITY_CUTOUT_NO_CULL);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_cutout_no_cull_z_offset"
        ))
    )
    private static void registerCustomEntityCutoutNoCullZOffset(final CallbackInfo callback) {
        SnsRenderPipelines.ENTITY_CUTOUT_NO_CULL_Z_OFFSET = register(SnsRenderPipelines.ENTITY_CUTOUT_NO_CULL_Z_OFFSET);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/item_entity_translucent_cull"
        ))
    )
    private static void registerCustomItemEntityTranslucentCull(final CallbackInfo callback) {
        SnsRenderPipelines.ITEM_ENTITY_TRANSLUCENT_CULL = register(SnsRenderPipelines.ITEM_ENTITY_TRANSLUCENT_CULL);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_translucent"
        ))
    )
    private static void registerCustomEntityTranslucent(final CallbackInfo callback) {
        SnsRenderPipelines.ENTITY_TRANSLUCENT = register(SnsRenderPipelines.ENTITY_TRANSLUCENT);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = M_REGISTER, ordinal = 0),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=pipeline/entity_no_outline"
        ))
    )
    private static void registerCustomEntityNoOutline(final CallbackInfo callback) {
        SnsRenderPipelines.ENTITY_NO_OUTLINE = register(SnsRenderPipelines.ENTITY_NO_OUTLINE);
    }
}

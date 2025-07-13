package io.github.startsmercury.simply_no_shading.impl.client.death_protection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import java.util.function.Consumer;

public class SnsRenderPipelines {
    /**
     * In the creation of a custom derived {@link RenderPipeline}, its builder
     * is reused.
     */
    public static final String M_BUILD = """
        Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder; \
        build (                                              \
        ) Lcom/mojang/blaze3d/pipeline/RenderPipeline;       \
    """;

    /**
     * A custom {@link RenderPipeline} is registered after its original.
     */
    public static final String M_REGISTER = """
        Lnet/minecraft/client/renderer/RenderPipelines;  \
        register (                                       \
            Lcom/mojang/blaze3d/pipeline/RenderPipeline; \
        ) Lcom/mojang/blaze3d/pipeline/RenderPipeline;   \
    """;

    public static RenderPipeline TRANSLUCENT;
    public static RenderPipeline ENTITY_SOLID;
    public static RenderPipeline ENTITY_CUTOUT;
    public static RenderPipeline ENTITY_CUTOUT_NO_CULL;
    public static RenderPipeline ENTITY_CUTOUT_NO_CULL_Z_OFFSET;
    public static RenderPipeline ITEM_ENTITY_TRANSLUCENT_CULL;
    public static RenderPipeline ENTITY_TRANSLUCENT;
    public static RenderPipeline ENTITY_NO_OUTLINE;


    public static RenderPipeline createNoShading(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original,
        final Consumer<? super RenderPipeline> setter
    ) {
        final var pipeline = original.call(builder);

        final var customLocation = pipeline
            .getLocation()
            .withPath(path -> path + SnsConstants.LUMINOUS_SUFFIX);
        final var customVertexShader = pipeline
            .getVertexShader()
            .withPath(path -> path + SnsConstants.NO_SHADING_SUFFIX);
        final var customPipeline = builder
            .withLocation(customLocation)
            .withVertexShader(customVertexShader)
            .build();
        setter.accept(customPipeline);

        return pipeline;
    }

    public static RenderPipeline createLuminous(
        final RenderPipeline.Builder builder,
        final Operation<RenderPipeline> original,
        final Consumer<? super RenderPipeline> setter
    ) {
        final var pipeline = original.call(builder);

        final var customLocation = pipeline
            .getLocation()
            .withPath(path -> path + SnsConstants.LUMINOUS_SUFFIX);
        final var customPipeline = builder
            .withLocation(customLocation)
            .withShaderDefine("NO_CARDINAL_LIGHTING")
            .build();
        setter.accept(customPipeline);

        return pipeline;
    }

    public static void t(final RenderPipeline translucent) {
        TRANSLUCENT = translucent;
    }

    public static void es(final RenderPipeline entitySolid) {
        ENTITY_SOLID = entitySolid;
    }

    public static void ec(final RenderPipeline entityCutout) {
        ENTITY_CUTOUT = entityCutout;
    }

    public static void ecnc(final RenderPipeline entityCutoutNoCull) {
        ENTITY_CUTOUT_NO_CULL = entityCutoutNoCull;
    }

    public static void ecnczo(final RenderPipeline entityCutoutNoCullZOffset) {
        ENTITY_CUTOUT_NO_CULL_Z_OFFSET = entityCutoutNoCullZOffset;
    }

    public static void ietc(final RenderPipeline itemEntityTranslucentCell) {
        ITEM_ENTITY_TRANSLUCENT_CULL = itemEntityTranslucentCell;
    }

    public static void et(final RenderPipeline entityTranslucent) {
        ENTITY_TRANSLUCENT = entityTranslucent;
    }

    public static void eno(final RenderPipeline entityNoOutline) {
        ENTITY_NO_OUTLINE = entityNoOutline;
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsCoreShaders;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CoreShaders.class)
public abstract class CoreShadersMixin {
    @Shadow
    private static ShaderProgram register(final String string, final VertexFormat vertexFormat) {
        throw new AssertionError();
    }

    @Shadow
    private static ShaderProgram register(
        final String string,
        final VertexFormat vertexFormat,
        final ShaderDefines shaderDefines
    ) {
        throw new AssertionError();
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
            Lnet/minecraft/client/renderer/CoreShaders;     \
            register (                                      \
                Ljava/lang/String;                          \
                Lcom/mojang/blaze3d/vertex/VertexFormat;    \
            ) Lnet/minecraft/client/renderer/ShaderProgram; \
        """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_entity_solid"
        ))
    )
    private static void registerCustomEntitySolid(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_ENTITY_SOLID = register(
            "rendertype_entity_solid_luminous",
            DefaultVertexFormat.NEW_ENTITY,
            ShaderDefines.builder().define("NO_CARDINAL_LIGHTING").build()
        );
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
            Lnet/minecraft/client/renderer/CoreShaders;     \
            register (                                      \
                Ljava/lang/String;                          \
                Lcom/mojang/blaze3d/vertex/VertexFormat;    \
            ) Lnet/minecraft/client/renderer/ShaderProgram; \
        """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_translucent"
        ))
    )
    private static void registerCustomTranslucent(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_TRANSLUCENT = register(
            "rendertype_translucent_no_shading",
            DefaultVertexFormat.BLOCK
        );
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
            Lnet/minecraft/client/renderer/CoreShaders;     \
            register (                                      \
                Ljava/lang/String;                          \
                Lcom/mojang/blaze3d/vertex/VertexFormat;    \
            ) Lnet/minecraft/client/renderer/ShaderProgram; \
        """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_entity_cutout"
        ))
    )
    private static void registerCustomEntityCutout(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_ENTITY_CUTOUT = register(
            "rendertype_entity_cutout_luminous",
            DefaultVertexFormat.NEW_ENTITY,
            ShaderDefines.builder().define("NO_CARDINAL_LIGHTING").build()
        );
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
            Lnet/minecraft/client/renderer/CoreShaders;     \
            register (                                      \
                Ljava/lang/String;                          \
                Lcom/mojang/blaze3d/vertex/VertexFormat;    \
            ) Lnet/minecraft/client/renderer/ShaderProgram; \
        """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_entity_cutout_no_cull"
        ))
    )
    private static void registerCustomEntityCutoutNoCull(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL = register(
            "rendertype_entity_cutout_no_cull_luminous",
            DefaultVertexFormat.NEW_ENTITY,
            ShaderDefines.builder().define("NO_CARDINAL_LIGHTING").build()
        );
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
            Lnet/minecraft/client/renderer/CoreShaders;     \
            register (                                      \
                Ljava/lang/String;                          \
                Lcom/mojang/blaze3d/vertex/VertexFormat;    \
            ) Lnet/minecraft/client/renderer/ShaderProgram; \
        """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_entity_cutout_no_cull_z_offset"
        ))
    )
    private static void registerCustomEntityCutoutZOffsetNoCull(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET = register(
            "rendertype_entity_cutout_no_cull_z_offset_luminous",
            DefaultVertexFormat.NEW_ENTITY,
            ShaderDefines.builder().define("NO_CARDINAL_LIGHTING").build()
        );
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
                Lnet/minecraft/client/renderer/CoreShaders;     \
                register (                                      \
                    Ljava/lang/String;                          \
                    Lcom/mojang/blaze3d/vertex/VertexFormat;    \
                ) Lnet/minecraft/client/renderer/ShaderProgram; \
            """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_item_entity_translucent_cull"
        ))
    )
    private static void registerCustomItemEntityTranslucentCull(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL = register(
        "rendertype_item_entity_translucent_cull_no_shading",
            DefaultVertexFormat.NEW_ENTITY
        );
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
                Lnet/minecraft/client/renderer/CoreShaders;     \
                register (                                      \
                    Ljava/lang/String;                          \
                    Lcom/mojang/blaze3d/vertex/VertexFormat;    \
                ) Lnet/minecraft/client/renderer/ShaderProgram; \
            """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_entity_translucent"
        ))
    )
    private static void registerCustomEntityTranslucent(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT = register(
            "rendertype_entity_translucent_luminous",
            DefaultVertexFormat.NEW_ENTITY,
            ShaderDefines.builder().define("NO_CARDINAL_LIGHTING").build()
        );
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = """
                Lnet/minecraft/client/renderer/CoreShaders;     \
                register (                                      \
                    Ljava/lang/String;                          \
                    Lcom/mojang/blaze3d/vertex/VertexFormat;    \
                ) Lnet/minecraft/client/renderer/ShaderProgram; \
            """,
            ordinal = 0
        ),
        slice = @Slice(from = @At(
            value = "CONSTANT",
            args = "stringValue=rendertype_entity_no_outline"
        ))
    )
    private static void registerCustomEntityNoOutline(final CallbackInfo callback) {
        SnsCoreShaders.RENDERTYPE_ENTITY_NO_OUTLINE = register(
            "rendertype_entity_no_outline_luminous",
            DefaultVertexFormat.NEW_ENTITY,
            ShaderDefines.builder().define("NO_CARDINAL_LIGHTING").build()
        );
    }
}

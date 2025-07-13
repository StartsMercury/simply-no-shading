package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.ResourceProviderWrapper;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {
    @Final
    @Mutable
    @Shadow
    private String name;

    @Inject(
        method = """
            <init>(\
                Lnet/minecraft/server/packs/resources/ResourceProvider;\
                Ljava/lang/String;\
                Lcom/mojang/blaze3d/vertex/VertexFormat;\
            )V\
        """,
        at = @At(
            value = "FIELD",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/renderer/ShaderInstance;name:Ljava/lang/String;",
            ordinal = 0,
            opcode = Opcodes.PUTFIELD
        )
    )
    private void changeName(
        final CallbackInfo callback,
        @Local(ordinal = 0, argsOnly = true) final ResourceProvider resourceProvider,
        @Local(ordinal = 0, argsOnly = true) final String string
    ) {
        if (resourceProvider instanceof ResourceProviderWrapper) {
            this.name = string + "_no_shade";
        }
    }

    @ModifyArg(
        method = """
            <init>(\
                Lnet/minecraft/server/packs/resources/ResourceProvider;\
                Ljava/lang/String;\
                Lcom/mojang/blaze3d/vertex/VertexFormat;\
            )V\
        """,
        at = @At(value = "INVOKE", ordinal = 1, target = """
            Lnet/minecraft/client/renderer/ShaderInstance;getOrCreate(\
                Lnet/minecraft/server/packs/resources/ResourceProvider;\
                Lcom/mojang/blaze3d/shaders/Program$Type;\
                Ljava/lang/String;\
            )Lcom/mojang/blaze3d/shaders/Program;\
        """)
    )
    private ResourceProvider wrapResourceProvider(
        final ResourceProvider resourceProvider,
        final Program.Type type,
        final String string
    ) {
        // TODO fix path to not get merged by vanilla equivalent shader
        if (
            type == Program.Type.FRAGMENT
                && resourceProvider instanceof ResourceProviderWrapper(final var inner)
        ) {
            return inner;
        } else {
            return resourceProvider;
        }
    }

    @ModifyArg(
        method = """
            getOrCreate(\
                Lnet/minecraft/server/packs/resources/ResourceProvider;\
                Lcom/mojang/blaze3d/shaders/Program$Type;\
                Ljava/lang/String;\
            )Lcom/mojang/blaze3d/shaders/Program;\
        """,
        at = @At(value = "INVOKE", target = """
            Lcom/mojang/blaze3d/shaders/Program;compileShader(\
                Lcom/mojang/blaze3d/shaders/Program$Type;\
                Ljava/lang/String;\
                Ljava/io/InputStream;\
                Ljava/lang/String;\
                Lcom/mojang/blaze3d/preprocessor/GlslPreprocessor;\
            )Lcom/mojang/blaze3d/shaders/Program;\
        """),
        index = 1
    )
    private static String changeName(
        final String name,
        final @Local(ordinal = 0, argsOnly = true) ResourceProvider resourceProvider
    ) {
        if (resourceProvider instanceof ResourceProviderWrapper) {
            return name + "_no_shade";
        } else {
            return name;
        }
    }
}

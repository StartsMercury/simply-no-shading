package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$CompilationCache")
public class ShaderManager$CompilationCacheMixin {
    @ModifyExpressionValue(
        method = """
            compileProgram (                                        \
                Lnet/minecraft/client/renderer/ShaderProgram;       \
            ) Lnet/minecraft/client/renderer/CompiledShaderProgram; \
        """,
        at = @At(value = "INVOKE", target = """
            Lnet/minecraft/client/renderer/ShaderProgram; \
            configId (                                    \
            ) Lnet/minecraft/resources/ResourceLocation;  \
        """)
    )
    private ResourceLocation changeProgramName(
            final ResourceLocation configId,
            final @Local(ordinal = 0, argsOnly = true) ShaderProgram program,
        final @Share("noShading") LocalBooleanRef noShading
    ) {
        if (configId.getPath().endsWith(SnsConstants.CUSTOM_SHADER_SUFFIX)) {
            noShading.set(true);
            return configId.withPath(path -> path.substring(
                0,
                path.length() - SnsConstants.CUSTOM_SHADER_SUFFIX.length()
            ));
        } else {
            return configId;
        }
    }

    @ModifyExpressionValue(
        method = """
            compileProgram (                                        \
                Lnet/minecraft/client/renderer/ShaderProgram;       \
            ) Lnet/minecraft/client/renderer/CompiledShaderProgram; \
        """,
        at = @At(value = "INVOKE", target = """
            Lnet/minecraft/client/renderer/ShaderProgramConfig; \
            vertex (                                            \
            ) Lnet/minecraft/resources/ResourceLocation;        \
        """)
    )
    private ResourceLocation changeVertexProgramName(
        final ResourceLocation configId,
        final @Local(ordinal = 0, argsOnly = true) ShaderProgram program,
        final @Share("noShading") LocalBooleanRef noShading
    ) {
        if (noShading.get()) {
            return configId.withPath(
                path -> path + SnsConstants.CUSTOM_SHADER_SUFFIX
            );
        } else {
            return configId;
        }
    }
}

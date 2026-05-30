package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.ShaderPreprocessor;
import io.github.startsmercury.simply_no_shading.impl.client.extension.GetShaderPreprocessor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @since 7.4.0
 */
@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$1")
public class ShaderManager$1Mixin implements GetShaderPreprocessor {
    @Override
    public @Nullable ShaderPreprocessor simply_no_shading$getShaderPreprocessor() {
        return ShaderPreprocessor.CONDITIONAL;
    }
}

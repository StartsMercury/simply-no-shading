package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.ShaderPreprocessor;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.ResourceProviderWrapper;
import io.github.startsmercury.simply_no_shading.impl.client.extension.GetShaderPreprocessor;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @since 7.4.0
 */
@Mixin(targets = "net.minecraft.client.renderer.ShaderInstance$1")
public class ShaderInstance$1Mixin implements GetShaderPreprocessor {
    @Final
    @Shadow
    ResourceProvider val$resourceProvider;

    @Override
    public ShaderPreprocessor simply_no_shading$getShaderPreprocessor() {
        return val$resourceProvider instanceof ResourceProviderWrapper
            ? ShaderPreprocessor.UNCONDITIONAL
            : ShaderPreprocessor.CONDITIONAL;
    }
}

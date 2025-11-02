package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import io.github.startsmercury.simply_no_shading.impl.client.ShaderPreprocessor;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.GetShaderPreprocessor;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlslPreprocessor.class)
public class GlslPreprocessorMixin implements GetShaderPreprocessor {
    @Unique
    private final ConfigData simply_no_shading$configData = Minecraft.getInstance().getSimplyNoShading().getConfig().data();

    /**
     * Modifies the {@code minecraft_mix_light} function in Minecraft's
     * {@code shaders/include/light.glsl} shader to enforce Simply No Shading
     * features.
     *
     * @param quotesUsed true for double-quote include; false for angle-brackets
     * @param file the file path to include
     * @param source the shader source code
     * @return the modified shader source code
     */
    @WrapOperation(
        method = "processImports",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/preprocessor/GlslPreprocessor;applyImport(ZLjava/lang/String;)Ljava/lang/String;"
        )
    )
    private String modifyLightingCalculations(
        final GlslPreprocessor self,
        final boolean quotesUsed,
        final String file,
        final Operation<String> operation,
        final @Local(ordinal = 0, argsOnly = true) String source
    ) {
        final var original = operation.call(self, quotesUsed, file);
        if (simply_no_shading$configData.shadeEntities()
            || quotesUsed
            || !ShaderPreprocessor.TARGET_SYSTEM_MOJ_IMPORT.equals(file)
        ) {
            return original;
        }

        final var preprocessor = this.simply_no_shading$getShaderPreprocessor();
        if (preprocessor == null || !preprocessor.containsAllKeywords(source)) {
            return original;
        }

        return preprocessor.process(original);
    }
}

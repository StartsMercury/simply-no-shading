package io.github.startsmercury.simply_no_shading.mixin.client.shading.shader.minecraft;

import java.util.regex.Pattern;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @since 7.4.0
 */
@Mixin(targets = "net.minecraft.client.renderer.ShaderInstance$1")
public abstract class ShaderInstance$1Mixin {
    /**
     * 
     */
    @Unique
    private static final Pattern FN_MINECRAFT_MIX_LIGHT = Pattern.compile(
        "vec4\\s+minecraft_mix_light\\s*\\(\\s*vec3\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*,\\s*vec3\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*,\\s*vec3\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*,\\s*vec4\\s+(?<color>[a-zA-Z_][a-zA-Z0-9_]*)\\s*\\)\\s*\\{"
    );

    /**
     * Modifies the {@code minecraft_mix_light} function in Minecraft's
     * {@code shaders/include/light.glsl} shader to enforce Simply No Shading
     * features.
     *
     * @param source the included shader source code
     * @param quotesUsed true for double-quote include; false for angle-brackets
     * @param file the file path to include
     * @return the modified shader source code
     */
    @ModifyReturnValue(
        method = "applyImport(ZLjava/lang/String;)Ljava/lang/String;",
        at = @At("RETURN")
    )
    private String modifyLightingCalculations(
        final String source,
        final boolean quotesUsed,
        final String file
    ) {
        if (quotesUsed || !file.equals("shaders/include/light.glsl")) {
            return source;
        }

        final var matcher = FN_MINECRAFT_MIX_LIGHT.matcher(source);
        if (!matcher.find()) {
            return source;
        }

        // TODO try redirecting calls to a wrapper function to properly handle toggling
        final var color = matcher.group("color");
        final var addition = "\n    if (true) { return " + color + "; }\n";
        return source.substring(0, matcher.end()) + addition + source.substring(matcher.end());
    }
}

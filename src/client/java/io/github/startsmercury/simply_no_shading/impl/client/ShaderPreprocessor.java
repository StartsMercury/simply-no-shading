package io.github.startsmercury.simply_no_shading.impl.client;

public enum ShaderPreprocessor {
    /** Disables in-world shading, filtering out GUI elements. */
    CONDITIONAL,
    UNCONDITIONAL;

    public static final String TARGET_SYSTEM_MOJ_IMPORT = "minecraft:light.glsl";

    public boolean containsAllKeywords(final String source) {
        return this == UNCONDITIONAL
            // This import provides the `ProjMat` needed for conditional lighting
            || source.contains("#moj_import <minecraft:projection.glsl>");
    }

    public String process(final String source) {
        return switch (this) {
            case CONDITIONAL -> source
                .replace("minecraft_mix_light(", "minecraft_mix_light_helper(")
                .replace("minecraft_mix_light_separate(", "minecraft_mix_light_separate_helper(") + """
            
            #define minecraft_mix_light(lightDir0, lightDir1, normal, color) (ProjMat[3].x != -1 ? color : minecraft_mix_light_helper(lightDir0, lightDir1, normal, color))
            #define minecraft_mix_light_separate(light, color) (ProjMat[3].x != -1 ? color : minecraft_mix_light_separate_helper(light, color))
            """;
            case UNCONDITIONAL -> source
                .replace("minecraft_mix_light(", "minecraft_mix_light_ignored(")
                .replace("minecraft_mix_light_separate(", "minecraft_mix_light_separate_ignored(") + """
            
            #define minecraft_mix_light(lightDir0, lightDir1, normal, color) color
            #define minecraft_mix_light_separate(light, color) color
            """;
        };
    }
}

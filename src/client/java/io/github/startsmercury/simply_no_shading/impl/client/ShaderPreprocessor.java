package io.github.startsmercury.simply_no_shading.impl.client;

import java.util.regex.Pattern;
import net.minecraft.util.StringUtil;

public enum ShaderPreprocessor {
    /** Disables in-world shading, filtering out GUI elements. */
    CONDITIONAL(
        "ProjMat[3].x != -1 ? color : ${helperFunctionName}(lightDir0, lightDir1, normal, color)",
        "_helper",
        "ProjMat"
    ),
    UNCONDITIONAL("color", "_ignored");

    public static final String TARGET_SYSTEM_MOJ_IMPORT = "minecraft:light.glsl";

    private static final String TARGET_FUNCTION_NAME = "minecraft_mix_light";

    private static final Pattern TARGET_FUNCTION_SIGNATURE_PATTERN;
    static {
        final var returnType = "vec4";
        final var parameterTypes = new String[] { "vec3", "vec3", "vec3", "vec4" };

        final var optionalWhitespace = "\\s*";
        final var requiredWhitespace = "\\s+";
        final var identifier = "[a-zA-Z_][a-zA-Z0-9_]*";

        final var builder = new StringBuilder();
        builder.append('(');
        builder.append(returnType);
        builder.append(requiredWhitespace);
        builder.append(TARGET_FUNCTION_NAME);
        builder.append(')');

        builder.append("\\(");
        final var parameterCount = parameterTypes.length;
        for (var i = 0; ; ) {
            builder.append(optionalWhitespace);
            builder.append(parameterTypes[i]);
            builder.append(requiredWhitespace);
            builder.append(identifier);
            builder.append(optionalWhitespace);
            if (++i >= parameterCount) {
                break;
            }
            builder.append(',');
        }
        builder.append("\\)");

        TARGET_FUNCTION_SIGNATURE_PATTERN = Pattern.compile(builder.toString());
    }

    private final String replacement;

    private final String suffix;

    private final String[] keywords;

    ShaderPreprocessor(final String macroCode, final String suffix, final String... keywords) {
        final var somehowDissociateFromSourceLineNumbering = "#line 0";
        final var helperFunctionName = TARGET_FUNCTION_NAME + suffix;
        final var template = """
            
            ${somehowDissociateFromSourceLineNumbering}
            #define ${TARGET_FUNCTION_NAME}(lightDir0, lightDir1, normal, color) (${macroCode})
            """;

        this.replacement = template.replace("${macroCode}", macroCode)
            .replace("${somehowDissociateFromSourceLineNumbering}", somehowDissociateFromSourceLineNumbering)
            .replace("${TARGET_FUNCTION_NAME}", TARGET_FUNCTION_NAME)
            .replace("${helperFunctionName}", helperFunctionName);
        this.suffix = suffix;
        this.keywords = keywords;
    }

    public boolean containsAllKeywords(final String source) {
        for (final var keyword : this.keywords) {
            if (!source.contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    public String process(final String source) {
        final var matcher = TARGET_FUNCTION_SIGNATURE_PATTERN.matcher(source);
        if (!matcher.find()) {
            return source;
        }

        final var correctLineNumber = StringUtil.lineCount(source.substring(0, matcher.start()));

        final var beforeTargetFunction = source.substring(0, matcher.start());
        final var ignoreChangesToLineNumbering = "\n#line " + correctLineNumber + "\n";
        final var returnTypeAndOriginalName = matcher.group(1);
        final var afterReturnTypeAndChangedName = source.substring(matcher.end(1));

        return beforeTargetFunction
            + this.replacement
            + ignoreChangesToLineNumbering
            + returnTypeAndOriginalName
            + this.suffix
            + afterReturnTypeAndChangedName;
    }
}

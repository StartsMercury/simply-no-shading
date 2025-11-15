package io.github.startsmercury.simply_no_shading.impl.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.ShaderPreprocessor;
import org.jspecify.annotations.Nullable;

public interface GetShaderPreprocessor {
    default @Nullable ShaderPreprocessor simply_no_shading$getShaderPreprocessor() {
        return null;
    }
}

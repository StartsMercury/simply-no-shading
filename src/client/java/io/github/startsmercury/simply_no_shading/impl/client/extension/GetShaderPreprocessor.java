package io.github.startsmercury.simply_no_shading.impl.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.ShaderPreprocessor;

public interface GetShaderPreprocessor {
    default ShaderPreprocessor simply_no_shading$getShaderPreprocessor() {
        return null;
    }
}

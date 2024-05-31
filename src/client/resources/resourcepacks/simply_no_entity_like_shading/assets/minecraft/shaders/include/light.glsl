#version 150

#define MINECRAFT_LIGHT_POWER   (0.6)
#define MINECRAFT_AMBIENT_LIGHT (0.4)

/**
 * Performs no color mixing.
 * <p>
 * Unlike the original {@code minecraft_mix_light} or similar implementations,
 * this method performs no color mixing and essentially keeps the original
 * color.
 *
 * @return the original color
 * @implNote This implementation is made for Simply No Shading's experimental
 *     support for entity shading and currently affects the ones rendered in the
 *     client GUI as well.
 */
vec4 minecraft_mix_light(vec3 _lightDir0, vec3 _lightDir1, vec3 _normal, vec4 color) {
    return color;
}

// from the original, may cause problems when other shaders expected a different
// definition provided by their or others' shader.
vec4 minecraft_sample_lightmap(sampler2D lightMap, ivec2 uv) {
    return texture(lightMap, clamp(uv / 256.0, vec2(0.5 / 16.0), vec2(15.5 / 16.0)));
}

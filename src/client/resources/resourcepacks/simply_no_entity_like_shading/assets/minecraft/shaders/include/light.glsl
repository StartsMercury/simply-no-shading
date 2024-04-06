#version 150

#define MINECRAFT_LIGHT_POWER   (0.6)
#define MINECRAFT_AMBIENT_LIGHT (0.4)

/*
 * Entity-like shading is disabled by redifining this very function. Ideally the
 * callers of this function should be modified to distinguish GUI entities and
 * world entities. The limitation of the previous sentence through simple
 * resource pack might be invasive with the overwriting nature and may not work
 * well with other core shaders. A proposed solution is by doing said through
 * transforming the shaders so that way and alike mix-ins would modify calls to
 * this very function while also calculating if it should forego overwriting
 * with disabled shading. It is to note that said solutions are not yet in
 * progress and this long comment is doing many work of documenting and as a
 * TODO for the future.
 */
vec4 minecraft_mix_light(vec3 lightDir0, vec3 lightDir1, vec3 normal, vec4 color) {
    return color;
}

// from the original, may cause problems when other shaders expected a different
// definition provided by their or others' shader.
vec4 minecraft_sample_lightmap(sampler2D lightMap, ivec2 uv) {
    return texture(lightMap, clamp(uv / 256.0, vec2(0.5 / 16.0), vec2(15.5 / 16.0)));
}

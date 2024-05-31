#version 150

#define MINECRAFT_LIGHT_POWER   (0.6)
#define MINECRAFT_AMBIENT_LIGHT (0.4)

/*
 * Entity-like shading is disabled by redifining this very function. Ideally the
 * callers of this function should be modified to distinguish GUI entities and
 * world entities. The implementation of the previous sentence through simple
 * resource pack might be invasive with its overwriting nature and may not work
 * well with other core shaders. A possible solution is by doing said through
 * transforming the shaders so that way, alike mix-ins, would modify calls to
 * this very function while also calculating if it should proceed to overwriting
 * shading. It is to note that said solutions are not yet in progress and this
 * long comment is doing many work such as documenting and an ellaborate TODO.
 */
vec4 minecraft_mix_light(vec3 _lightDir0, vec3 _lightDir1, vec3 _normal, vec4 color) {
    return color;
}

// from the original, may cause problems when other shaders expected a different
// definition provided by their or others' shader.
vec4 minecraft_sample_lightmap(sampler2D lightMap, ivec2 uv) {
    return texture(lightMap, clamp(uv / 256.0, vec2(0.5 / 16.0), vec2(15.5 / 16.0)));
}

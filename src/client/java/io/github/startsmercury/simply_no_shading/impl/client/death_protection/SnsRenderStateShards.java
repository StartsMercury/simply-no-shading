package io.github.startsmercury.simply_no_shading.impl.client.death_protection;

import net.minecraft.client.renderer.RenderStateShard;

public final class SnsRenderStateShards {
    public static void init() {}

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_TRANSLUCENT_SHADER =
        new RenderStateShard.ShaderStateShard(SnsCoreShaders.RENDERTYPE_TRANSLUCENT);

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_SOLID_SHADER =
        new RenderStateShard.ShaderStateShard(SnsCoreShaders.RENDERTYPE_ENTITY_SOLID);

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_SHADER =
        new RenderStateShard.ShaderStateShard(SnsCoreShaders.RENDERTYPE_ENTITY_CUTOUT);

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER =
        new RenderStateShard.ShaderStateShard(SnsCoreShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL);

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER =
        new RenderStateShard.ShaderStateShard(SnsCoreShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET);

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER =
        new RenderStateShard.ShaderStateShard(
            SnsCoreShaders.RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL
        );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TRANSLUCENT_SHADER  =
        new RenderStateShard.ShaderStateShard(SnsCoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT);

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_NO_OUTLINE_SHADER  =
        new RenderStateShard.ShaderStateShard(SnsCoreShaders.RENDERTYPE_ENTITY_NO_OUTLINE);
}

package io.github.startsmercury.simply_no_shading.impl.client.death_protection;

import net.minecraft.client.renderer.RenderStateShard;

public final class SnsRenderStateShards {
    public static void init() {}

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_SOLID_SHADER =
        new RenderStateShard.ShaderStateShard(
            DeathProtectionFeature::getRendertypeEntitySolidShader
        );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_SHADER =
        new RenderStateShard.ShaderStateShard(
            DeathProtectionFeature::getRendertypeEntityCutoutShader
        );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER =
        new RenderStateShard.ShaderStateShard(
            DeathProtectionFeature::getRendertypeEntityCutoutNoCullShader
        );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER =
        new RenderStateShard.ShaderStateShard(
            DeathProtectionFeature::getRendertypeEntityCutoutNoCullZOffsetShader
        );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER =
        new RenderStateShard.ShaderStateShard(
            DeathProtectionFeature::getRendertypeItemEntityTranslucentCullShader
        );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER =
        new RenderStateShard.ShaderStateShard(
            DeathProtectionFeature::getRendertypeEntityTranslucentCullShader
        );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_NO_OUTLINE_SHADER =
        new RenderStateShard.ShaderStateShard(
            DeathProtectionFeature::getRendertypeEntityNoOutlineShader
        );
}

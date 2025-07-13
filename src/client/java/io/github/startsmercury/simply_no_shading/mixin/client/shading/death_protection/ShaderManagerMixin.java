package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.CompiledShader;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsGlslPreprocessor;
import java.util.Map;
import net.minecraft.FileUtil;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.ShaderProgramConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public abstract class ShaderManagerMixin {
    @Inject(
        method = """
            loadShader (                                         \
                Lnet/minecraft/resources/ResourceLocation;       \
                Lnet/minecraft/server/packs/resources/Resource;  \
                Lcom/mojang/blaze3d/shaders/CompiledShader$Type; \
                Ljava/util/Map;                                  \
                Lcom/google/common/collect/ImmutableMap$Builder; \
            ) V                                                  \
        """,
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, remap = false, target = """
            Lcom/google/common/collect/ImmutableMap$Builder;   \
            put (                                              \
                Ljava/lang/Object;                             \
                Ljava/lang/Object;                             \
            ) Lcom/google/common/collect/ImmutableMap$Builder; \
        """)
    )
    private static void loadCustomShader(
        final CallbackInfo callback,
        final @Local(ordinal = 0, argsOnly = true) ResourceLocation resourceLocation,
        final @Local(ordinal = 0, argsOnly = true) CompiledShader.Type type,
        final @Local(ordinal = 0, argsOnly = true) Map<ResourceLocation, Resource> map,
        final @Local(ordinal = 0, argsOnly = true) ImmutableMap.Builder<
            ShaderManager.ShaderSourceKey,
            String
        > builder,
        final @Local(ordinal = 1) ResourceLocation resourceLocation2,
        final @Local(ordinal = 0) String string
    ) {
        switch (resourceLocation.getPath()) {
            case "shaders/core/rendertype_item_entity_translucent_cull.vsh",
                 "shaders/core/terrain.vsh":
                break;
            default:
                return;
        }

        final var glslPreprocessor = new SnsGlslPreprocessor(
            resourceLocation.withPath(FileUtil::getFullResourcePath),
            map
        );

        builder.put(
            new ShaderManager.ShaderSourceKey(
                resourceLocation2.withPath(
                    path -> path + SnsConstants.CUSTOM_SHADER_SUFFIX
                ),
                type
            ),
            String.join("", glslPreprocessor.process(string))
        );
    }

    @Inject(
        method = "loadProgram",
        at = @At(
            value = "INVOKE",
            target = """
                Lcom/google/common/collect/ImmutableMap$Builder;   \
                put (                                              \
                    Ljava/lang/Object;                             \
                    Ljava/lang/Object;                             \
                ) Lcom/google/common/collect/ImmutableMap$Builder; \
            """,
            remap = false
        )
    )
    private static void loadCustomProgram(
        final CallbackInfo callback,
        final @Local(ordinal = 0, argsOnly = true) ResourceLocation resourceLocation,
        final @Local(ordinal = 0, argsOnly = true) Resource resource,
        final @Local(ordinal = 0, argsOnly = true) ImmutableMap.Builder<ResourceLocation, ShaderProgramConfig> builder,
        final @Local(ordinal = 1) ResourceLocation resourceLocation2,
        final @Local(ordinal = 0) ShaderProgramConfig config
    ) {
        switch (resourceLocation.getPath()) {
            case "shaders/core/rendertype_entity_cutout.json",
                 "shaders/core/rendertype_entity_cutout_no_cull.json",
                 "shaders/core/rendertype_entity_cutout_no_cull_z_offset.json",
                 "shaders/core/rendertype_entity_no_outline.json",
                 "shaders/core/rendertype_entity_solid.json",
                 "shaders/core/rendertype_entity_translucent.json":
                break;
            default:
                return;
        }

        final var resourceLocation3 = resourceLocation2.withPath(
            path -> path + "_luminous"
        );

        builder.put(resourceLocation3, config);
    }
}

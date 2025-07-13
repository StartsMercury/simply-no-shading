package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.DeathProtectionFeature;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.ResourceProviderWrapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(
        method = "renderItemActivationAnimation",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;<init>()V"
        )
    )
    private void onRenderItemActivationAnimationStart(final CallbackInfo callback) {
        final var simplyNoShading = (SimplyNoShadingImpl) this.minecraft.getSimplyNoShading();
        simplyNoShading.context().setItemActivationItem(true);
    }

    @Inject(
        method = "renderItemActivationAnimation",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"
        )
    )
    private void onRenderItemActivationAnimationEnd(final CallbackInfo callback) {
        final var simplyNoShading = (SimplyNoShadingImpl) this.minecraft.getSimplyNoShading();
        simplyNoShading.context().setItemActivationItem(false);
    }

    @Inject(
        method = "reloadShaders(Lnet/minecraft/server/packs/resources/ResourceManager;)V",
        at = @At(
            value = "INVOKE",
            slice = "after_rendertype_entity_translucent_cull",
            shift = At.Shift.AFTER,
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            id = "after_rendertype_entity_translucent_cull",
            from = @At(value = "CONSTANT", args = "stringValue=rendertype_entity_translucent_cull")
        )
    )
    private void withReloadShader(
        final CallbackInfo callback,
        final @Local(ordinal = 0, argsOnly = true) ResourceManager resourceManager,
        final @Local(ordinal = 1) List<Pair<ShaderInstance, Consumer<ShaderInstance>>> list2
    ) throws IOException {
        list2.add(Pair.of(
            new ShaderInstance(
                new ResourceProviderWrapper(resourceManager),
                "rendertype_entity_solid",
                DefaultVertexFormat.NEW_ENTITY
            ),
            shaderInstance -> DeathProtectionFeature.rendertypeEntitySolidShader = shaderInstance
        ));
        list2.add(Pair.of(
            new ShaderInstance(
                new ResourceProviderWrapper(resourceManager),
                "rendertype_entity_cutout",
                DefaultVertexFormat.NEW_ENTITY
            ),
            shaderInstance -> DeathProtectionFeature.rendertypeEntityCutoutShader = shaderInstance
        ));
        list2.add(Pair.of(
            new ShaderInstance(
                new ResourceProviderWrapper(resourceManager),
                "rendertype_entity_cutout_no_cull",
                DefaultVertexFormat.NEW_ENTITY
            ),
            shaderInstance -> {
                DeathProtectionFeature.rendertypeEntityCutoutNoCullShader = shaderInstance;
            }
        ));
        list2.add(Pair.of(
            new ShaderInstance(
                new ResourceProviderWrapper(resourceManager),
                "rendertype_entity_cutout_no_cull_z_offset",
                DefaultVertexFormat.NEW_ENTITY
            ),
            shaderInstance -> {
                DeathProtectionFeature.rendertypeEntityCutoutNoCullZOffsetShader = shaderInstance;
            }
        ));
        list2.add(Pair.of(
            new ShaderInstance(
                new ResourceProviderWrapper(resourceManager),
                "rendertype_item_entity_translucent_cull",
                DefaultVertexFormat.NEW_ENTITY
            ),
            shaderInstance -> {
                DeathProtectionFeature.rendertypeItemEntityTranslucentCullShader = shaderInstance;
            }
        ));
        list2.add(Pair.of(
            new ShaderInstance(
                new ResourceProviderWrapper(resourceManager),
                "rendertype_entity_translucent_cull",
                DefaultVertexFormat.NEW_ENTITY
            ),
            shaderInstance -> {
                DeathProtectionFeature.rendertypeEntityTranslucentCullShader = shaderInstance;
            }
        ));
        list2.add(Pair.of(
            new ShaderInstance(
                new ResourceProviderWrapper(resourceManager),
                "rendertype_entity_no_outline",
                DefaultVertexFormat.NEW_ENTITY
            ),
            shaderInstance -> {
                DeathProtectionFeature.rendertypeEntityNoOutlineShader = shaderInstance;
            }
        ));
    }
}

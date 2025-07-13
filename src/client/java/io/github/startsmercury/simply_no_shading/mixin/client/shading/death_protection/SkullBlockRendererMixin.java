package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import io.github.startsmercury.simply_no_shading.impl.client.death_protection.DeathProtectionFeature;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SkullBlockRenderer.class)
public class SkullBlockRendererMixin {
    @ModifyArg(
        method = "getRenderType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderType;entityCutoutNoCullZOffset(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"
        ),
        index = 0
    )
    private static ResourceLocation modifyEntityCutoutNoCullZOffset(final ResourceLocation original) {
        return DeathProtectionFeature.tryMangle(original);
    }

    @ModifyArg(
        method = "getRenderType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderType;entityTranslucent(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"
        ),
        index = 0
    )
    private static ResourceLocation modifyEntityTranslucent(final ResourceLocation original) {
        return DeathProtectionFeature.tryMangle(original);
    }
}

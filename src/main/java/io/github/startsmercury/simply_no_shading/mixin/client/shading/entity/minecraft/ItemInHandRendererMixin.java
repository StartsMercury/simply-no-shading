package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.minecraft;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.blaze3d.platform.GlStateManager;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "renderPlayerArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/resources/ResourceLocation;)V"))
    private void disablePlayerArmLighting(
        final CallbackInfo callback,
        final @Share("overrideLighting") LocalBooleanRef overrideLightingRef
    ) {
        if (!SimplyNoShading.instance().config().entityShadingEnabled()) {
            GlStateManager.disableLighting();
            overrideLightingRef.set(true);
        }
    }

    @Inject(method = "renderPlayerArm", at = @At("RETURN"))
    private void reEnablePlayerArmLighting(
        final CallbackInfo callbackInfo,
        final @Share("overrideLighting") LocalBooleanRef overrideLightingRef
    ) {
        if (overrideLightingRef.get()) {
            GlStateManager.enableLighting();
        }
    }
}

package com.github.startsmercury.simply.no.shading.mixin.shading.entity.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(GlStateManager.class)
public abstract class GlStateManagerMixin {
    /**
     * This is an {@linkplain Inject injector} that runs additional code at the
     * head of {@link GlStateManager#enableLighting()}.
     * <p>
     * Cancels turning back lighting to on when {@linkplain
     * Config#entityShadingEnabled entity shading is disabled}.
     *
     * @param callback the callback
     */
    @Inject(method = "enableLighting()V", at = @At("HEAD"), cancellable = true)
    private final static void ignoreEnablingLighting(final CallbackInfo callback) {
        if (!SimplyNoShading.getFirstInstance().getConfig().entityShadingEnabled)
            callback.cancel();
    }
}

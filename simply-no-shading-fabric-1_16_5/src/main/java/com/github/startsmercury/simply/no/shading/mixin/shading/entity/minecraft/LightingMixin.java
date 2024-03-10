package com.github.startsmercury.simply.no.shading.mixin.shading.entity.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;
import com.mojang.blaze3d.platform.Lighting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * The {@code LightingMixin} is a {@linkplain Mixin mixin} class for the
 * {@link Lighting} class.
 *
 * @since 6.1.0
 */
@Environment(EnvType.CLIENT)
@Mixin(Lighting.class)
public abstract class LightingMixin {
    /**
     * This is an {@linkplain Inject injector} that runs additional code at the
     * head of {@link Lighting#turnBackOn()}.
     * <p>
     * Cancels turning back lighting to on when {@linkplain
     * Config#entityShadingEnabled entity shading is disabled}.
     *
     * @param callback the callback
     */
    @Inject(method = "turnBackOn()V", at = @At("HEAD"), cancellable = true)
    private static void cancelTurnBackOn(final CallbackInfo callback) {
        if (!SimplyNoShading.getFirstInstance().getConfig().entityShadingEnabled)
            callback.cancel();
    }

    /**
     * A private constructor that does nothing as of the writing of this
     * documentation.
     */
    private LightingMixin() {
    }
}

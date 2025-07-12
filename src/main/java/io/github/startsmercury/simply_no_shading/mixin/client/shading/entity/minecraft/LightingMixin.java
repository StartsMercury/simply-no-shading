package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Lighting;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Lighting.class)
public class LightingMixin {
    @WrapMethod(method = { "turnBackOn()V", "turnOff()V" })
    private static void conditionalToggling(final Operation<Void> original) {
        final SimplyNoShadingImpl simplyNoShading = (SimplyNoShadingImpl) Minecraft.getInstance().getSimplyNoShading();
        if (simplyNoShading.isLightingForceOff()) {
            // DO NOTHING
        } else {
            original.call();
        }
    }
}

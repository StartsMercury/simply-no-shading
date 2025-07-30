package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @WrapMethod(method = "renderLevel(FJ)V")
    private void modifyEntityLighting(
        final float partialTick,
        final long nanos,
        final Operation<Void> original
    ) {
        final SimplyNoShadingImpl simplyNoShading = (SimplyNoShadingImpl) minecraft.getSimplyNoShading();
        simplyNoShading.lightingScope(() -> original.call(partialTick, nanos));
    }

    @WrapMethod(method = "renderItemActivationAnimation(IIF)V")
    private void modifyEntityLighting(
        final int width,
        final int height,
        final float partialTick,
        final Operation<Void> original
    ) {
        final SimplyNoShadingImpl simplyNoShading = (SimplyNoShadingImpl) minecraft.getSimplyNoShading();
        simplyNoShading.lightingScope(() -> original.call(width, height, partialTick));
    }
}

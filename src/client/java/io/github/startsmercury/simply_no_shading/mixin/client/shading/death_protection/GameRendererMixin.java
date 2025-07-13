package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
        method = "renderItemActivationAnimation",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;pose()Lcom/mojang/blaze3d/vertex/PoseStack;"
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
}

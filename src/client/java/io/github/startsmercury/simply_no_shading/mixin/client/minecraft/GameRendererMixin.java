package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    private GameRenderState gameRenderState;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "extractOptions()V", at = @At("RETURN"))
    private void extractSimplyNoShading(final CallbackInfo callback) {
        final var simplyNoShadingState = this.gameRenderState.optionsRenderState.simplyNoShadingRenderState();
        final var config = this.minecraft.getSimplyNoShading().getConfig();
        simplyNoShadingState.compatibilityMode = config.compatibilityMode();
        final var data = config.data();
        simplyNoShadingState.shadeBlocks = data.shadeBlocks();
        simplyNoShadingState.shadeClouds = data.shadeClouds();
        simplyNoShadingState.shadeEntities = data.shadeEntities();
    }
}

package io.github.startsmercury.simply_no_shading.mixin.shading.block.minecraft;

import io.github.startsmercury.simply_no_shading.client.SimplyNoShadingUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @since 6.2.0
 */
@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
    @Final
    @Mutable
    @Shadow
    private boolean shade;

    private BakedQuadMixin() {
    }

    /* TODO
    @Inject(method = "<init>([IILnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Z)V", at = @At("RETURN"))
    private void changeInitialShade(final CallbackInfo callback) {
        this.shade = this.shade && SimplyNoShadingUtils.ComputedConfig.blockShadingEnabled;
    }*/

    @Inject(method = "isShade()Z", at = @At("RETURN"), cancellable = true)
    private void changeReturnedShade(final CallbackInfoReturnable<Boolean> callback) {
        // TODO try FaceBakery, BlockModel, ModelBlockRenderer, BlockRenderDispatcher, SectionRenderDispatcher, LevelRenderer

        callback.setReturnValue(callback.getReturnValueZ() && SimplyNoShadingUtils.ComputedConfig.blockShadingEnabled);
    }
}

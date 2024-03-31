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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
    @Final
    @Mutable
    @Shadow
    private boolean shade;

    private BakedQuadMixin() {
    }

    // TODO
    @Inject(method = "init", at = @At("RETURN"))
    private void changeInitialShade() {
        this.shade = this.shade && SimplyNoShadingUtils.ComputedConfig.blockShadingEnabled;
    }

    @Inject(method = "isShade()Z", at = @At("RETURN"), cancellable = true)
    private void changeReturnedShade(final CallbackInfoReturnable<Boolean> callback) {
        // TODO try FaceBakery, BlockModel, ModelBlockRenderer, BlockRenderDispatcher, SectionRenderDispatcher, LevelRenderer

        // TEMP: callback.setReturnValue(callback.getReturnValueZ() && ComputedConfig.blockShadingEnabled);
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.sodium;

import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class MixinBakedQuadMixin {
    @Dynamic("from me.jellysquid.mods.sodium.mixin.core.pipeline.MixinBakedQuad")
    @Inject(
        method = "getColor(I)I",
        at = @At("RETURN"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void changeReturnedColor(
        final int idx,
        final CallbackInfoReturnable<Integer> callback
    ) {
        if (!SimplyNoShading.instance().config().blockShadingEnabled()) {
            callback.setReturnValue(0xFFFFFFFF);
        }
    }
}

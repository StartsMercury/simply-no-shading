package io.github.startsmercury.simply_no_shading.mixin.client.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.fabric.block.FabricBlockAccess;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FabricBlockAccess.class)
public class FabricBlockAccessMixin {
    @ModifyVariable(
        method = "normalShade(Lnet/minecraft/client/renderer/block/BlockAndTintGetter;FFFZ)F",
        at = @At("HEAD"),
        argsOnly = true
    )
    private boolean modifyShade(
        final boolean hasShade,
        final @Local(ordinal = 0, argsOnly = true) BlockAndTintGetter blockView
    ) {
        return hasShade && blockView.simply_no_shading$configData().shadeBlocks();
    }
}

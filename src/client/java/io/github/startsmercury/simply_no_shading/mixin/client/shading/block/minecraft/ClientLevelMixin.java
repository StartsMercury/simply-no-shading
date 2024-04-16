package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @since 6.2.0
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    private ClientLevelMixin() {
    }

    @ModifyVariable(
        method = "getShade(Lnet/minecraft/core/Direction;Z)F",
        at = @At("HEAD"),
        argsOnly = true
    )
    private boolean changeShade(final boolean shade) {
        return shade && ComputedConfig.blockShadingEnabled;
    }
}

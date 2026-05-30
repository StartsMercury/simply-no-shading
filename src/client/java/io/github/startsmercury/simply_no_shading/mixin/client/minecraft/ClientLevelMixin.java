package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    private ClientLevelMixin() {
    }

    @ModifyVariable(
        method = "getShade(Lnet/minecraft/core/Direction;Z)F",
        at = @At("HEAD"),
        argsOnly = true
    )
    private boolean simply_no_shading$changeShade(final boolean shade) {
        return shade && SimplyNoShadingImpl.renderStateOf(this.minecraft).shadeBlocks;
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.CardinalLighting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    private ClientLevelMixin() {}

    @ModifyReturnValue(
        method = "cardinalLighting()Lnet/minecraft/world/level/CardinalLighting;",
        at = @At("RETURN")
    )
    private CardinalLighting processCardinalLighting(final CardinalLighting original) {
        if (SimplyNoShadingImpl.renderStateOf(this.minecraft).shadeBlocks) {
            return original;
        } else {
            return SnsConstants.CARDINAL_LIGHTING;
        }
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
    private BakedQuadMixin() {
    }

    @ModifyReturnValue(method = "shade()Z", at = @At("RETURN"))
    private boolean modifyShade(final boolean original) {
        // This usually only gets called during meshing; hopefully not every frame
        final var config = Minecraft.getInstance().getSimplyNoShading().getConfig();

        if (config.compatibilityMode()) {
            // Injecting into ClientLevel.getShade may not be sufficient, thus:
            return original && config.data().shadeBlocks();
        } else {
            return original;
        }
    }
}

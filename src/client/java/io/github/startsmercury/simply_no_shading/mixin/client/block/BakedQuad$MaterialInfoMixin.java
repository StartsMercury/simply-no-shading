package io.github.startsmercury.simply_no_shading.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// NOTE: Currently unused, preferred to more controlled methods, like not
//       directly injecting into CardinalLighting, this allows ConfigData to
//       flow through instead of directly accessing it. I guess.
@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.MaterialInfo.class)
public abstract class BakedQuad$MaterialInfoMixin {
    private BakedQuad$MaterialInfoMixin() {
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

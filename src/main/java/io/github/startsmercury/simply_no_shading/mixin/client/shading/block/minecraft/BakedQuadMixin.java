package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
    private BakedQuadMixin() {
    }

    @ModifyReturnValue(method = "isShade()Z", at = @At("RETURN"))
    private boolean modifyShade(final boolean original) {
        // TODO try FaceBakery, BlockModel, ModelBlockRenderer, BlockRenderDispatcher, SectionRenderDispatcher, LevelRenderer

        return original && ComputedConfig.blockShadingEnabled;
    }
}

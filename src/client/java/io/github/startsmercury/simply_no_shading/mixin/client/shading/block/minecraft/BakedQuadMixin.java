package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @since 6.2.0
 */
@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
    /*@Final
    @Mutable
    @Shadow
    private boolean shade;*/

    private BakedQuadMixin() {
    }

    /* TODO
    @Inject(method = "<init>([IILnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Z)V", at = @At("RETURN"))
    private void changeInitialShade(final CallbackInfo callback) {
        this.shade = this.shade && SimplyNoShadingUtils.ComputedConfig.blockShadingEnabled;
    }*/

    @ModifyReturnValue(method = "isShade()Z", at = @At("RETURN"))
    private boolean modifyShade(final boolean original) {
        // TODO try FaceBakery, BlockModel, ModelBlockRenderer, BlockRenderDispatcher, SectionRenderDispatcher, LevelRenderer

        return original && ComputedConfig.blockShadingEnabled;
    }
}

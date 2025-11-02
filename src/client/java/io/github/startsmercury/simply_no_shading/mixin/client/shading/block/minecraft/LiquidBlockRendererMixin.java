package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = LiquidBlockRenderer.class, priority = 999)
public class LiquidBlockRendererMixin implements SnsConfigDataAware {
    @Unique
    private ConfigData simply_no_shading$configData = ConfigData.VANILLA;

    private LiquidBlockRendererMixin() {
    }

    @Override
    public ConfigData simply_no_shading$getConfigData() {
        return this.simply_no_shading$configData;
    }

    @Override
    public void simply_no_shading$setConfigData(final ConfigData configData) {
        this.simply_no_shading$configData = configData;
    }

    @ModifyArg(
        method = """
            tesselate(\
                Lnet/minecraft/world/level/BlockAndTintGetter;\
                Lnet/minecraft/core/BlockPos;\
                Lcom/mojang/blaze3d/vertex/VertexConsumer;\
                Lnet/minecraft/world/level/block/state/BlockState;\
                Lnet/minecraft/world/level/material/FluidState;\
            )V\
        """,
        at = @At(
            value = "INVOKE",
            target = """
                Lnet/minecraft/world/level/BlockAndTintGetter;getShade(\
                    Lnet/minecraft/core/Direction;\
                    Z\
                )F\
            """
        ),
        index = 1
    )
    private boolean simply_no_shading$changeShade(final boolean shade) {
        return shade && this.simply_no_shading$getConfigData().shadeBlocks();
    }
}

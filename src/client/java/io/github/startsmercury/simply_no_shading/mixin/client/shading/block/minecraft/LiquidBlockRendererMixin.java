package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.world.level.CardinalLighting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LiquidBlockRenderer.class, priority = 999)
public class LiquidBlockRendererMixin implements SnsConfigDataAware {
    @Unique
    private ConfigData simply_no_shading$configData = SimplyNoShadingImpl.DEFAULT_CONFIG_DATA;

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

    @Inject(
        method = "tesselate(" +
            "Lnet/minecraft/client/renderer/block/BlockAndTintGetter;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lcom/mojang/blaze3d/vertex/VertexConsumer;" +
            "Lnet/minecraft/world/level/block/state/BlockState;" +
            "Lnet/minecraft/world/level/material/FluidState;" +
        ")V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/renderer/block/BlockAndTintGetter;" +
                "cardinalLighting()Lnet/minecraft/world/level/CardinalLighting;"
        )
    )
    private void simply_no_shading$accessConfigToScope(
        final CallbackInfo callback,

        final @Share("shadeBlocks") LocalBooleanRef shadeBlocksRef
    ) {
        shadeBlocksRef.set(this.simply_no_shading$configData.shadeBlocks());
    }

    @WrapOperation(
        method = "tesselate(" +
            "Lnet/minecraft/client/renderer/block/BlockAndTintGetter;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lcom/mojang/blaze3d/vertex/VertexConsumer;" +
            "Lnet/minecraft/world/level/block/state/BlockState;" +
            "Lnet/minecraft/world/level/material/FluidState;" +
        ")V",
        at = {
            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;down()F"
            ),            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;north()F"
            ),            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;south()F"
            ),            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;west()F"
            ),            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/CardinalLighting;east()F"
            ),
        }
    )
    private float simply_no_shading$changeShade(
        final CardinalLighting cardinalLighting,
        final Operation<Float> original,
        final @Share("shadeBlocks") LocalBooleanRef shadeBlocksRef
    ) {
        return shadeBlocksRef.get() ? original.call(cardinalLighting) : cardinalLighting.up();
    }
}

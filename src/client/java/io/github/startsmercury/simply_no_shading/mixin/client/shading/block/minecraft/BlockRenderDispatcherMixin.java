package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockRenderDispatcher.class)
public class BlockRenderDispatcherMixin implements SnsConfigDataAware {
    @Shadow
    private @Nullable LiquidBlockRenderer liquidBlockRenderer;
    @Unique
    private ConfigData simply_no_shading$configData = SimplyNoShadingImpl.DEFAULT_CONFIG_DATA;

    private BlockRenderDispatcherMixin() {
    }

    @Override
    public ConfigData simply_no_shading$getConfigData() {
        return this.simply_no_shading$configData;
    }

    @Override
    public void simply_no_shading$setConfigData(final ConfigData configData) {
        this.simply_no_shading$configData = configData;

        final var liquidBlockRenderer = this.liquidBlockRenderer;
        if (liquidBlockRenderer != null) {
            ((SnsConfigDataAware) liquidBlockRenderer).simply_no_shading$setConfigData(configData);
        }
    }

    @WrapOperation(
        method = "onResourceManagerReload",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;liquidBlockRenderer:Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;",
            opcode = Opcodes.PUTFIELD
        )
    )
    public void simply_no_shading$initConfigForLiquidBlockRenderer(
        final BlockRenderDispatcher instance,
        final LiquidBlockRenderer value,
        final Operation<Void> original
    ) {
        ((SnsConfigDataAware) value).simply_no_shading$setConfigData(this.simply_no_shading$getConfigData());
        original.call(instance, value);
    }

}

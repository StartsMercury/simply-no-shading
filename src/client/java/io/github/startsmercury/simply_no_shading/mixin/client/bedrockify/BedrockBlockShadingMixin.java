package io.github.startsmercury.simply_no_shading.mixin.client.bedrockify;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataOwner;
import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockBlockShading;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BedrockBlockShading.class)
@Pseudo
public class BedrockBlockShadingMixin implements SnsConfigDataOwner {
    @Unique
    private ConfigData simply_no_shading$configData = ConfigData.VANILLA;

    private BedrockBlockShadingMixin() {
    }

    @Override
    public @NonNull ConfigData simply_no_shading$configData() {
        return this.simply_no_shading$configData;
    }

    @Override
    public void simply_no_shading$setConfigData(final @NonNull ConfigData configData) {
        this.simply_no_shading$configData = configData;
    }

    @ModifyReturnValue(
        method = {
            "getBlockShade(Lnet/minecraft/core/Direction;)F",
            "getLiquidShade(Lnet/minecraft/core/Direction;Z)F"
        },
        at = @At("RETURN")
    )
    private float modifyShade(final float original) {
        if (this.simply_no_shading$configData.shadeBlocks()) {
            return original;
        } else {
            return Math.max(1.0f, original);
        }
    }
}

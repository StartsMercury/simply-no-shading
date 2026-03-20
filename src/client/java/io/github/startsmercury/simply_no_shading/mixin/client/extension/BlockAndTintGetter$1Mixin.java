package io.github.startsmercury.simply_no_shading.mixin.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.client.renderer.block.BlockAndTintGetter$1")
public class BlockAndTintGetter$1Mixin implements SnsConfigDataAware {
    private BlockAndTintGetter$1Mixin() {}

    @Override
    public ConfigData simply_no_shading$configData() {
        return ConfigData.VANILLA;
    }
}

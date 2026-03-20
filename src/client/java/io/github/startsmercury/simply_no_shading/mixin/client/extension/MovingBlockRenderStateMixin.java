package io.github.startsmercury.simply_no_shading.mixin.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.compile.CompileSnsConfigDataOwner;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MovingBlockRenderState.class)
@SuppressWarnings("deprecation")
public class MovingBlockRenderStateMixin implements CompileSnsConfigDataOwner {
    @Unique
    private ConfigData configData = ConfigData.VANILLA;

    private MovingBlockRenderStateMixin() {}

    @Override
    public ConfigData simply_no_shading$configData() {
        return this.configData;
    }

    @Override
    public void simply_no_shading$setConfigData(final ConfigData configData) {
        this.configData = configData;
    }
}

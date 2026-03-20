package io.github.startsmercury.simply_no_shading.mixin.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderSectionRegion.class)
public class RenderSectionRegionMixin implements SnsConfigDataAware {
    @Final
    @Shadow
    private ClientLevel level;

    private RenderSectionRegionMixin() {}

    @Override
    public ConfigData simply_no_shading$configData() {
        return this.level.simply_no_shading$configData();
    }
}

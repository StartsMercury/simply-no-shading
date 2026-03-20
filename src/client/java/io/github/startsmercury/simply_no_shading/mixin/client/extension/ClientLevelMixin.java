package io.github.startsmercury.simply_no_shading.mixin.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.compile.CompileSnsConfigDataAware;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientLevel.class)
@SuppressWarnings("deprecation")
public class ClientLevelMixin implements CompileSnsConfigDataAware {
    @Final
    @Shadow
    private LevelRenderer levelRenderer;

    private ClientLevelMixin() {}

    @Override
    public ConfigData simply_no_shading$configData() {
        return this.levelRenderer.simply_no_shading$configData();
    }
}

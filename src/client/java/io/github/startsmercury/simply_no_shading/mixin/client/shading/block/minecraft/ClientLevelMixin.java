package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements SnsConfigDataAware {
    @Unique
    private ConfigData simply_no_shading$configData = ConfigData.VANILLA;

    private ClientLevelMixin() {
    }

    @Override
    public ConfigData simply_no_shading$getConfigData() {
        return this.simply_no_shading$configData;
    }

    @Override
    public void simply_no_shading$setConfigData(final ConfigData configData) {
        this.simply_no_shading$configData = configData;
    }

    @ModifyVariable(
        method = "getShade(Lnet/minecraft/core/Direction;Z)F",
        at = @At("HEAD"),
        argsOnly = true
    )
    private boolean simply_no_shading$changeShade(final boolean shade) {
        return shade && this.simply_no_shading$configData.shadeBlocks();
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.sodium;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelSlice.class)
public class LevelSliceMixin implements SnsConfigDataAware {
    @Final
    @Shadow
    private ClientLevel level;

    private LevelSliceMixin() {}

    @Override
    public ConfigData simply_no_shading$configData() {
        return this.level.simply_no_shading$configData();
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.extension.compile.MinecraftExtension;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MinecraftExtension {
    @Unique
    private final SimplyNoShadingImpl simplyNoShading = new SimplyNoShadingImpl((Minecraft) (Object) this);

    private MinecraftMixin() {}

    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public SimplyNoShadingImpl getSimplyNoShading() {
        return this.simplyNoShading;
    }
}

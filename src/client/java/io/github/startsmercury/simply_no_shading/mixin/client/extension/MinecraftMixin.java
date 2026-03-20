package io.github.startsmercury.simply_no_shading.mixin.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.extension.compile.CompileSimplyNoShadingAware;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Minecraft.class)
@SuppressWarnings("deprecation")
public class MinecraftMixin implements CompileSimplyNoShadingAware {
    @Unique
    private final SimplyNoShadingImpl simplyNoShading = new SimplyNoShadingImpl((Minecraft) (Object) this);

    private MinecraftMixin() {}

    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public SimplyNoShadingImpl getSimplyNoShading() {
        return this.simplyNoShading;
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.minecraft;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingRenderState;
import io.github.startsmercury.simply_no_shading.impl.client.extension.compile.OptionsRenderStateExtension;
import net.minecraft.client.renderer.state.OptionsRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(OptionsRenderState.class)
public class OptionsRenderStateMixin implements OptionsRenderStateExtension {
    @Unique
    private final SimplyNoShadingRenderState simplyNoShading = new SimplyNoShadingRenderState();

    @Override
    public SimplyNoShadingRenderState simplyNoShadingRenderState() {
        return this.simplyNoShading;
    }
}

package io.github.startsmercury.simply_no_shading.mixin.client.compat.iris;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import net.irisshaders.iris.config.IrisConfig;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IrisConfig.class)
public class IrisConfigMixin {
    @Shadow(remap = false)
    private boolean enableShaders;

    @Inject(method = "setShadersEnabled(Z)V", at = @At("RETURN"), remap = false)
    private void onSetShadersEnabled(final boolean enabled, final CallbackInfo callback) {
        final var simplyNoShading = (SimplyNoShadingImpl) Minecraft.getInstance().getSimplyNoShading();
        simplyNoShading.context().setShadersEnabled(enabled);
    }

    @Inject(
        method = "load()V",
        at = @At(
            value = "FIELD",
            shift = At.Shift.AFTER,
            target = "Lnet/irisshaders/iris/config/IrisConfig;enableShaders:Z",
            opcode = Opcodes.PUTFIELD
        ),
        remap = false
    )
    private void onLoadShadersEnabled(final CallbackInfo callback) {
        final var simplyNoShading = (SimplyNoShadingImpl) Minecraft.getInstance().getSimplyNoShading();
        simplyNoShading.context().setShadersEnabled(this.enableShaders);
    }
}

//TODO Re-enable when unobfuscated Iris Shaders is available
//package io.github.startsmercury.simply_no_shading.mixin.client.compat.iris;
//
//import net.irisshaders.iris.config.IrisConfig;
//import net.minecraft.client.Minecraft;
//import org.objectweb.asm.Opcodes;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(IrisConfig.class)
//public class IrisConfigMixin {
//    @Shadow(remap = false)
//    private boolean enableShaders;
//
//    @Inject(method = "setShadersEnabled(Z)V", at = @At("RETURN"), remap = false)
//    private void onSetShadersEnabled(final boolean enabled, final CallbackInfo callback) {
//        Minecraft.getInstance().getSimplyNoShading().getContext().setShadersEnabled(enabled);
//    }
//
//    @Inject(
//        method = "load()V",
//        at = @At(
//            value = "FIELD",
//            shift = At.Shift.AFTER,
//            target = "Lnet/irisshaders/iris/config/IrisConfig;enableShaders:Z",
//            opcode = Opcodes.PUTFIELD
//        ),
//        remap = false
//    )
//    private void onLoadShadersEnabled(final CallbackInfo callback) {
//        Minecraft.getInstance().getSimplyNoShading().getContext().setShadersEnabled(this.enableShaders);
//    }
//}

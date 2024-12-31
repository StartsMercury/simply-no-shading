package io.github.startsmercury.simply_no_shading.mixin.client.core.keymapping;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.startsmercury.simply_no_shading.impl.client.AwareKeyMapping;
import net.minecraft.client.KeyMapping;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements AwareKeyMapping {
    @WrapOperation(
        method = { "set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V", "setAll()V" },
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/KeyMapping;isDown:Z",
            opcode = Opcodes.PUTFIELD
        )
    )
    private static void dispatchSetEvent(
        final KeyMapping instance,
        final boolean value,
        final Operation<Void> original
    ) {
        ((AwareKeyMapping) instance).onSetDown(value);
        original.call(instance, value);
    }

    @WrapOperation(method = "release", at = @At(
        value = "FIELD",
        target = "Lnet/minecraft/client/KeyMapping;isDown:Z",
        opcode = Opcodes.PUTFIELD
    ))
    private void dispatchReleaseEvent(
        final KeyMapping instance,
        final boolean value,
        final Operation<Void> original
    ) {
        this.onSetDown(value);
        original.call(instance, value);
    }
}

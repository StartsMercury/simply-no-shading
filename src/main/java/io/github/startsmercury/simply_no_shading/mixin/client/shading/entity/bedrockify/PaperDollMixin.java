package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.bedrockify;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import me.juancarloscp52.bedrockify.client.features.paperDoll.PaperDoll;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PaperDoll.class)
public class PaperDollMixin {
    @WrapMethod(method = "renderPaperDoll(Lcom/mojang/blaze3d/vertex/PoseStack;)V")
    private void modifyEntityLighting(final PoseStack matrixStack, final Operation<Void> original) {
        SimplyNoShadingImpl.instance().lightingScope(() -> original.call(matrixStack));
    }
}

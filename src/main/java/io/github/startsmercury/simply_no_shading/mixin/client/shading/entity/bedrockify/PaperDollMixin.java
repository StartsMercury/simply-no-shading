package io.github.startsmercury.simply_no_shading.mixin.client.shading.entity.bedrockify;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import me.juancarloscp52.bedrockify.client.features.paperDoll.PaperDoll;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PaperDoll.class)
public class PaperDollMixin {
    @Final
    @Shadow
    private Minecraft client;

    @WrapMethod(method = "renderPaperDoll(Lcom/mojang/blaze3d/vertex/PoseStack;)V")
    private void modifyEntityLighting(final PoseStack matrixStack, final Operation<Void> original) {
        final var simplyNoShading = (SimplyNoShadingImpl) client.getSimplyNoShading();
        simplyNoShading.lightingScope(() -> original.call(matrixStack));
    }
}

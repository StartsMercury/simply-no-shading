package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.death_protection.DeathProtectionFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.MissingItemModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MissingItemModel.class)
public class MissingItemModelMixin {
    @ModifyExpressionValue(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/Sheets;cutoutBlockSheet()Lnet/minecraft/client/renderer/RenderType;"
        )
    )
    private RenderType modifyActivatedItemRenderType(
        final RenderType original,
        final @Local(ordinal = 0, argsOnly = true) ItemStack itemStack
    ) {
        final var minecraft = Minecraft.getInstance();
        final var simplyNoShading = minecraft.getSimplyNoShading();
        if (simplyNoShading.config().entityShadingEnabled()) return original;

        final var simplyNoShadingImpl = (SimplyNoShadingImpl) simplyNoShading;
        if (!simplyNoShadingImpl.context().itemActivationItem()) return original;

        return DeathProtectionFeature.modifyItemRenderType(simplyNoShadingImpl, original);
    }
}

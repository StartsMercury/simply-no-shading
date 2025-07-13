package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import io.github.startsmercury.simply_no_shading.impl.client.death_protection.DeathProtectionFeature;
import java.util.function.Function;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ ShieldModel.class, TridentModel.class })
public class ShieldModelAndTridentModelMixin {
    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/Model;<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V"
        ),
        index = 1
    )
    private static Function<ResourceLocation, RenderType> modifyRenderType(
        final Function<ResourceLocation, RenderType> function
    ) {
        return function.compose(DeathProtectionFeature::tryMangle);
    }
}

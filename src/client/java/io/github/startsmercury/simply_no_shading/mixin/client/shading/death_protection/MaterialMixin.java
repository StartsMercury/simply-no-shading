package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import io.github.startsmercury.simply_no_shading.impl.client.extension.MaterialAltRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Material.class)
public class MaterialMixin implements MaterialAltRenderType {
    @Nullable
    @Shadow
    private RenderType renderType;

    @Nullable
    @Unique
    private RenderType altRenderType;

    @Override
    public void simply_no_shading$altRenderType() {
        final var first = this.renderType;
        final var second = this.altRenderType;

        this.renderType = second;
        this.altRenderType = first;
    }
}

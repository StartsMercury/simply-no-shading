package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsRenderStateShards;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderStateShard.class)
public class RenderStateShardMixin {
    static {
        SnsRenderStateShards.init();
    }
}

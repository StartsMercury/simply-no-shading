package io.github.startsmercury.simply_no_shading.mixin.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.extension.compile.CompileSnsConfigDataAware;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockAndTintGetter.class)
@SuppressWarnings("deprecation")
public interface BlockAndTintGetterMixin extends CompileSnsConfigDataAware {
}

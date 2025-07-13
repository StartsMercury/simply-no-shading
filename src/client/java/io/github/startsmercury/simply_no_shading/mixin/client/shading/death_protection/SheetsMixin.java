package io.github.startsmercury.simply_no_shading.mixin.client.shading.death_protection;

import io.github.startsmercury.simply_no_shading.impl.client.death_protection.SnsSheets;
import net.minecraft.client.renderer.Sheets;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Sheets.class)
public class SheetsMixin {
    static {
        SnsSheets.init();
    }
}

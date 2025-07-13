package io.github.startsmercury.simply_no_shading.impl.client.extension;

import net.minecraft.client.resources.model.Material;

public interface MaterialAltRenderType {
    static void altRenderType(final Material self) {
        ((MaterialAltRenderType) self).simply_no_shading$altRenderType();
    }

    void simply_no_shading$altRenderType();
}

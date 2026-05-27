package io.github.startsmercury.simply_no_shading.impl.client.extension.compile;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingRenderState;

public interface OptionsRenderStateExtension {
    default SimplyNoShadingRenderState simplyNoShadingRenderState() {
        return InjectedInterfaceHelper.unimplemented();
    }
}

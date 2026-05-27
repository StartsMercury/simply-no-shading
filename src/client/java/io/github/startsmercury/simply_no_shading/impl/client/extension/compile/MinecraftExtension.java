package io.github.startsmercury.simply_no_shading.impl.client.extension.compile;

import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;

public interface MinecraftExtension {
    default SimplyNoShadingImpl getSimplyNoShading() {
        return InjectedInterfaceHelper.unimplemented();
    }
}

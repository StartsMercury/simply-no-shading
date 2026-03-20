package io.github.startsmercury.simply_no_shading.impl.client.extension.compile;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;

@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public interface CompileSnsConfigDataAware extends SnsConfigDataAware {
    @Override
    default ConfigData simply_no_shading$configData() {
        return InjectedInterfaceHelper.unimplemented();
    }
}

package io.github.startsmercury.simply_no_shading.impl.client.extension.compile;

import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataOwner;

@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public interface CompileSnsConfigDataOwner extends CompileSnsConfigDataAware, SnsConfigDataOwner {
    @Override
    default void simply_no_shading$setConfigData(final ConfigData configData) {
        InjectedInterfaceHelper.unimplemented();
    }
}

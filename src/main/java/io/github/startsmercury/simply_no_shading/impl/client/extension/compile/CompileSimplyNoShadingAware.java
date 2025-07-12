package io.github.startsmercury.simply_no_shading.impl.client.extension.compile;

import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SimplyNoShadingAware;

/**
 * Loom interface injectable version of {@code SimplyNoShadingAware}.
 *
 * @see SimplyNoShadingAware
 * @deprecated Prefer using {@code SimplyNoShadingAware} outside interface
 *     injection purposes.
 */
@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public interface CompileSimplyNoShadingAware extends SimplyNoShadingAware {
    @Override
    default SimplyNoShading getSimplyNoShading() {
        return CompileAwareHelper.unimplemented();
    }
}

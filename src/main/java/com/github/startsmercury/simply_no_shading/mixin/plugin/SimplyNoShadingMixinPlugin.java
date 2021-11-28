package com.github.startsmercury.simply_no_shading.mixin.plugin;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.github.startsmercury.simply_no_shading.entrypoint.SimplyNoShadingClient;

/**
 * Allows mixins that can be applied to present classes
 *
 * @see com.github.startsmercury.simply_no_shading.mixin.plugin
 * @see IMixinConfigPlugin
 * @see #shouldApplyMixin(String, String)
 */
public class SimplyNoShadingMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void onLoad(final String mixinPackage) {
	}

	@Override
	public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName,
			final IMixinInfo mixinInfo) {
	}

	@Override
	public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName,
			final IMixinInfo mixinInfo) {
	}

	/**
	 * Checks if a target class exist.
	 *
	 * @return {@code true} if a target class exist; {@code false} otherwise
	 * @see SimplyNoShadingMixinPlugin
	 * @see Class#forName(String)
	 */
	@Override
	public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
		try {
			Class.forName(targetClassName);

			return true;
		} catch (final ClassNotFoundException cnse) {
			SimplyNoShadingClient.LOGGER.debug(targetClassName + " is non existent, skipping...");
			SimplyNoShadingClient.LOGGER.catching(Level.TRACE, cnse);

			return false;
		}
	}
}

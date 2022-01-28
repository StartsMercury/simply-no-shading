package com.github.startsmercury.simplynoshading.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;

/**
 * A simple {@linkplain IMixinConfigPlugin mixin plugin} that applies all mixins
 * under this if the given mod(s) {@linkplain FabricLoader#isModLoaded(String)
 * are loaded}.
 */
public class CompatibilityMixinPlugin implements IMixinConfigPlugin {
	/**
	 * Cached flag if it should apply all mixins under this plugin.
	 */
	private final boolean shouldApply;

	/**
	 * Creates a new mixin plugin which applies all mixins under it.
	 */
	public CompatibilityMixinPlugin() {
		this.shouldApply = true;
	}

	/**
	 * Creates a new mixin plugin which applies all mixins under it if the given
	 * mod's id {@linkplain FabricLoader#isModLoaded(String) is loaded}.
	 *
	 * @param modid the target mod's id
	 */
	public CompatibilityMixinPlugin(final String modid) {
		this.shouldApply = FabricLoader.getInstance().isModLoaded(modid);
	}

	/**
	 * Creates a new mixin plugin which applies all mixins under it if all of the
	 * given mod(s)' id {@linkplain FabricLoader#isModLoaded(String) are loaded}.
	 *
	 * @param modids the target mod(s)' id
	 */
	public CompatibilityMixinPlugin(final String... modids) {
		boolean mayApply;

		mayApply = true;

		for (int i = 0; mayApply && i < modids.length; i++) {
			mayApply &= FabricLoader.getInstance().isModLoaded(modids[i]);
		}

		this.shouldApply = mayApply;
	}

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

	@Override
	public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
		return this.shouldApply;
	}
}

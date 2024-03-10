package com.github.startsmercury.simply.no.shading.mixin;

import static com.github.startsmercury.simply.no.shading.client.SimplyNoShading.LOGGER;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Simply No Shading {@link IMixinConfigPlugin mixin plugin}.
 *
 * @since 5.0.0
 */
public class SimplyNoShadingMixinPlugin implements IMixinConfigPlugin {
	/**
	 * Creates a new SimplyNoShadingMixinPlugin.
	 *
	 * @since 5.0.0
	 */
	public SimplyNoShadingMixinPlugin() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getMixins() {
		final ObjectArrayList<String> mixins = new ObjectArrayList<String>(3);
		includeMixins(mixins);
		mixins.trim();

		switch (mixins.size()) {
		case 0:
			break;
		case 1:
			LOGGER.info("[Simply No Shading] Included mixin '" + mixins.get(0) + "' due to their target mod being present");
			break;
		default:
			LOGGER.info("[Simply No Shading] Included mixins: ");
			mixins.forEach(mixin -> LOGGER.info("[Simply No Shading]     " + mixin));
			LOGGER.info("[Simply No Shading] Above mixins were added due to their target mod being present");
			break;
		}

		return mixins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRefMapperConfig() {
		return null;
	}

	/**
	 * Includes additional mixins.
	 *
	 * @param mixins the additional mixin list
	 */
	protected void includeMixins(final List<String> mixins) {
		final FabricLoader fabricLoader = FabricLoader.getInstance();

		if (fabricLoader.isModLoaded("sodium"))
			mixins.add("shading.block.sodium.FluidRendererMixin");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoad(final String mixinPackage) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postApply(final String targetClassName,
	        final ClassNode targetClass,
	        final String mixinClassName,
	        final IMixinInfo mixinInfo) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preApply(final String targetClassName,
	        final ClassNode targetClass,
	        final String mixinClassName,
	        final IMixinInfo mixinInfo) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
		return true;
	}
}

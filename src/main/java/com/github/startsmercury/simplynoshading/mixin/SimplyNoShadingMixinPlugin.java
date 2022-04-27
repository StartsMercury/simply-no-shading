package com.github.startsmercury.simplynoshading.mixin;

import static com.github.startsmercury.simplynoshading.SimplyNoShading.MIXIN_LOGGER;
import static com.github.startsmercury.simplynoshading.SimplyNoShading.getMixinConfig;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class SimplyNoShadingMixinPlugin implements IMixinConfigPlugin {
	private final ObjectOpenHashSet<String> excluded;

	public SimplyNoShadingMixinPlugin() {
		this.excluded = new ObjectOpenHashSet<>();

		for (final var element : getMixinConfig().getExcluded()) {
			final var excludee = "com.github.startsmercury.simplynoshading.mixin." + element;

			if (!excludee.matches("\\w+(?:\\w+)*")) {
				continue;
			}

			this.excluded.add(excludee);
		}

		this.excluded.trim();
	}

	@Override
	public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		final var mixins = new ObjectArrayList<String>();

		// TODO yes

		mixins.trim();

		return mixins;
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
		System.err.println(mixinClassName);

		for (final var excludee : this.excluded) {
			if (!mixinClassName.startsWith(excludee)) {
				continue;
			}

			final var offset = excludee.length();
			var startsWithExcludee = false;

			if (mixinClassName.length() == offset || (startsWithExcludee = mixinClassName.charAt(offset) == '.')) {
				final var message = new StringBuilder("Disabling ").append(mixinClassName);

				if (startsWithExcludee) {
					message.append(" given it's a member of ").append(excludee);
				}

				MIXIN_LOGGER.debug(message.toString());

				return false;
			}
		}

		return true;
	}
}

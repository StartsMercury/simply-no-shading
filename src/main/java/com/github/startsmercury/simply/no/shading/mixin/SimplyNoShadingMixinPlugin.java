package com.github.startsmercury.simply.no.shading.mixin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingMixinConfig;
import com.github.startsmercury.simply.no.shading.util.Constants;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.loader.api.FabricLoader;

public class SimplyNoShadingMixinPlugin implements IMixinConfigPlugin {
	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir()
	    .resolve("simply-no-shading+mixin.json");

	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading+mixin");

	private static SimplyNoShadingMixinConfig createMixinConfig() {
		final var mixinConfig = new SimplyNoShadingMixinConfig();

		try (final var out = Files.newBufferedWriter(CONFIG_PATH)) {
			LOGGER.debug("Creating mixin config...");

			Constants.GSON.toJson(mixinConfig, out);

			LOGGER.info("Created mixin config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to create mixin config", ioe);
		}

		return mixinConfig;
	}

	protected static SimplyNoShadingMixinConfig loadMixinConfig() {
		try (var in = Files.newBufferedReader(CONFIG_PATH)) {
			LOGGER.debug("Loading mixin config...");

			final var mixinConfig = Constants.GSON.fromJson(in, SimplyNoShadingMixinConfig.class);

			LOGGER.info("Loaded mixin config");

			return mixinConfig;
		} catch (final NoSuchFileException nsfe) {
			return createMixinConfig();
		} catch (final IOException ioe) {
			LOGGER.info("Unable to load mixin config", ioe);

			return new SimplyNoShadingMixinConfig();
		}
	}

	private final ObjectOpenHashSet<String> excludedCached;

	public SimplyNoShadingMixinPlugin() {
		LOGGER.debug("Constructing mixin plugin...");

		this.excludedCached = new ObjectOpenHashSet<>();
		final var mixinConfig = loadMixinConfig();

		for (final var element : mixinConfig.getExcluded()) {
			final var excludee = "com.github.startsmercury.simplynoshading.mixin." + element;

			if (!excludee.matches("\\w+(?:\\w+)*")) {
				continue;
			}

			this.excludedCached.add(excludee);
		}

		this.excludedCached.trim();

		LOGGER.info("Constructed mixin plugin");
	}

	@Override
	public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		final var mixins = new ObjectArrayList<String>();

		// TODO logic in adding mod dependent or version dependent mixins

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
		for (final var excludee : this.excludedCached) {
			if (!mixinClassName.startsWith(excludee)) {
				continue;
			}

			final var offset = excludee.length();
			var startsWithExcludee = false;

			if (mixinClassName.length() == offset || (startsWithExcludee = mixinClassName.charAt(offset) == '.')) {
				final var message = new StringBuilder("Excluded mixin ").append(mixinClassName);

				if (startsWithExcludee) {
					message.append(" given it's a member of ").append(excludee);
				}

				LOGGER.debug(message.toString());

				return false;
			}
		}

		return true;
	}
}

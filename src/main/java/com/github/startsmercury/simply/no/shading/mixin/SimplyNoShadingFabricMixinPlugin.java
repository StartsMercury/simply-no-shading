package com.github.startsmercury.simply.no.shading.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Simply No Shading fabric {@link IMixinConfigPlugin mixin plugin}.
 *
 * @since 5.0.0
 */
public class SimplyNoShadingFabricMixinPlugin extends SimplyNoShadingMixinPlugin {
	/**
	 * Creates a new SimplyNoShadingFabricMixinPlugin.
	 */
	public SimplyNoShadingFabricMixinPlugin() {
		super(false);

		// LOGGER.info("Constructed mixin plugin");
	}

	/**
	 * Creates a new SimplyNoShadingFabricMixinPlugin.
	 *
	 * @param log should log post construction
	 * @since 5.0.0
	 */
	protected SimplyNoShadingFabricMixinPlugin(final boolean log) {
		super(false);

		if (log) {
			// LOGGER.info("Constructed mixin plugin");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void includeMixins(final List<String> mixins) {
		final var fabricLoader = FabricLoader.getInstance();

		if (fabricLoader.isModLoaded("bedrockify")) {
			mixins.add("shading.liquid.bedrockify.BedrockBlockShadingFabricMixin");
		}

		if (fabricLoader.isModLoaded("enhancedblockentities")) {
			mixins.add("shading.enhanced.block.entity.enhancedblockentities.DynamicBakedModelFabricMixin");
		}

		if (fabricLoader.isModLoaded("fabric-renderer-api-v1")) {
			mixins.add("shading.frapi.models.fabric.renderer.api.v1.ClientLevelFabricMixin");
		}

		if (fabricLoader.isModLoaded("sodium")) { mixins.add("shading.liquid.sodium.FluidRendererFabricMixin"); }
	}
}

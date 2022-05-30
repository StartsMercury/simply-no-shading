package com.github.startsmercury.simply.no.shading.mixin;

import java.util.List;

import net.fabricmc.loader.api.FabricLoader;

public class SimplyNoShadingFabricMixinPlugin extends SimplyNoShadingMixinPlugin {
	public SimplyNoShadingFabricMixinPlugin() {
		super(false);

		LOGGER.info("Constructed mixin plugin");
	}

	protected SimplyNoShadingFabricMixinPlugin(final boolean log) {
		super(false);

		if (log) {
			LOGGER.info("Constructed mixin plugin");
		}
	}

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

		if (fabricLoader.isModLoaded("sodium")) {
			mixins.add("shading.liquid.sodium.FluidRendererFabricMixin");
		}
	}
}

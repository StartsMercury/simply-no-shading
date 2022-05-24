package com.github.startsmercury.simply.no.shading.mixin;

import java.util.List;

import net.fabricmc.loader.api.FabricLoader;

public class SimplyNoShadingFabricMixinPlugin extends SimplyNoShadingMixinPlugin {
	public SimplyNoShadingFabricMixinPlugin() {
	}

	@Override
	protected void includeMixins(final List<String> mixins) {
		final var fabricLoader = FabricLoader.getInstance();

		if (fabricLoader.isModLoaded("bedrockify")) {
			mixins.add("shading.liquid.bedrockify.BedrockBlockShadingMixin");
		}

		if (fabricLoader.isModLoaded("enhancedblockentities")) {
			mixins.add("shading.enhanced.block.entity.enhancedblockentities.DynamicBakedModelMixin");
		}

		if (fabricLoader.isModLoaded("sodium")) {
			mixins.add("shading.liquid.sodium.FluidRendererMixin");
		}
	}
}

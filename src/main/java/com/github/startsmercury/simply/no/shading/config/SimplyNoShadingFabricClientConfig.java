package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public class SimplyNoShadingFabricClientConfig extends SimplyNoShadingClientConfig {
	public class Observation<T extends SimplyNoShadingFabricClientConfig>
	    extends SimplyNoShadingClientConfig.Observation<T> {
		@Override
		public boolean rebuildChunks() {
			return super.rebuildChunks() || this.past.enhancedBlockEntityShading
			    .wouldEquals(SimplyNoShadingFabricClientConfig.this.enhancedBlockEntityShading);
		}
	}

	public final ShadingRule enhancedBlockEntityShading;

	public SimplyNoShadingFabricClientConfig() {
		this.enhancedBlockEntityShading = new ShadingRule.Child(this.allShading, true);
	}

	public SimplyNoShadingFabricClientConfig(final SimplyNoShadingClientConfig other) {
		this();

		copyFrom(other);
	}

	@Override
	public SimplyNoShadingFabricClientConfig copy() {
		return new SimplyNoShadingFabricClientConfig(this);
	}

	@Override
	public Observation<? extends SimplyNoShadingFabricClientConfig> observe() {
		return new Observation<>();
	}
}

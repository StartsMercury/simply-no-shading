package com.github.startsmercury.simply.no.shading;

import java.util.function.Supplier;

import com.github.startsmercury.simply.no.shading.api.ClientMod;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;

public interface SimplyNoShadingClientMod extends ClientMod, SimplyNoShadingCommonMod {
	Supplier<SimplyNoShadingClientMod> INSTANCE = new Supplier<>() {
		private SimplyNoShadingClientMod value;

		@Override
		public SimplyNoShadingClientMod get() {
			return this.value == null ? this.value = resolve() : this.value;
		}

		@SuppressWarnings("unused")
		private SimplyNoShadingClientMod resolve() {
			final SimplyNoShadingClientMod value = null;

			if (value == null) {
				throw new IllegalStateException("Unable to load any instance of SimplyNoShadingClientMod");
			}

			return value;
		}
	};

	static SimplyNoShadingClientMod getInstance() {
		return INSTANCE.get();
	}

	@Override
	SimplyNoShadingClientConfig getConfig();
}

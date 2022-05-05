package com.github.startsmercury.simply.no.shading.impl;

import java.util.function.Supplier;

import com.github.startsmercury.simply.no.shading.SimplyNoShadingClientMod;
import com.github.startsmercury.simply.no.shading.api.ClientFabricMod;
import com.github.startsmercury.simply.no.shading.api.CommonMod;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;

import net.fabricmc.api.ClientModInitializer;

public class SimplyNoShadingClientFabricMod extends AbstractCommonMod<SimplyNoShadingClientConfig, ClientModInitializer>
		implements ClientFabricMod, SimplyNoShadingClientMod {
	public static final Supplier<SimplyNoShadingClientFabricMod> INSTANCE = new Supplier<>() {
		private Object value = false;

		@Override
		public SimplyNoShadingClientFabricMod get() {
			return (SimplyNoShadingClientFabricMod) (this.value instanceof Boolean ? this.value = resolve()
					: this.value);
		}

		private SimplyNoShadingClientFabricMod resolve() {
			return new SimplyNoShadingClientFabricMod();
		}
	};

	protected static final ClientModInitializer createInitializer(final CommonMod mod) {
		return () -> {
			mod.getLogger().debug("Initializing Mod...");

			mod.loadConfig();

			mod.getLogger().info("Mod Initialized");
		};
	}

	public SimplyNoShadingClientFabricMod() {
		super(SimplyNoShadingClientConfig.class, SimplyNoShadingClientFabricMod::createInitializer);
	}

	public SimplyNoShadingClientFabricMod(final boolean createLogger) {
		super(SimplyNoShadingClientConfig.class, SimplyNoShadingClientFabricMod::createInitializer, createLogger);
	}

	public SimplyNoShadingClientFabricMod(final SimplyNoShadingClientConfig config) {
		super(config, SimplyNoShadingClientFabricMod::createInitializer);
	}

	public SimplyNoShadingClientFabricMod(final SimplyNoShadingClientConfig config, final boolean createLogger) {
		super(config, SimplyNoShadingClientFabricMod::createInitializer, createLogger);
	}

	@Override
	public void loadConfig() {
		getConfig().set(loadConfigInternally());
	}
}

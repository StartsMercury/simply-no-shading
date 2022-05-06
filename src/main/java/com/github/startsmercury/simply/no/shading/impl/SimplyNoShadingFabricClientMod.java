package com.github.startsmercury.simply.no.shading.impl;

import static com.github.startsmercury.simply.no.shading.util.KeyMappingUtils.keyMapping;
import static com.github.startsmercury.simply.no.shading.util.KeyMappingUtils.toggleKeyMapping;
import static com.github.startsmercury.simply.no.shading.util.Value.constant;
import static com.github.startsmercury.simply.no.shading.util.Value.unified;
import static com.mojang.blaze3d.platform.InputConstants.KEY_F6;

import com.github.startsmercury.simply.no.shading.SimplyNoShadingClientMod;
import com.github.startsmercury.simply.no.shading.api.CommonMod;
import com.github.startsmercury.simply.no.shading.api.FabricClientMod;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.util.Value;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

public class SimplyNoShadingFabricClientMod extends AbstractCommonMod<SimplyNoShadingClientConfig, ClientModInitializer>
		implements FabricClientMod, SimplyNoShadingClientMod {
	public static final Value<SimplyNoShadingFabricClientMod> INSTANCE = constant(SimplyNoShadingFabricClientMod::new);

	protected static final ClientModInitializer createInitializer(final CommonMod commonMod) {
		return () -> {
			if (!(commonMod instanceof final SimplyNoShadingFabricClientMod mod)) {
				return;
			}

			mod.getLogger().debug("Initializing Mod...");

			mod.unified.unlock();

			mod.loadConfig();

			mod.getLogger().info("Mod Initialized");
		};
	}

	public static final SimplyNoShadingFabricClientMod getInstance() {
		return INSTANCE.get();
	}

	public final Value<ToggleKeyMapping> blockShadingKey;

	public final Value<KeyMapping> openSettingsKey;

	public final Value<ToggleKeyMapping> shadingKey;

	private final Value.Unified unified;

	public SimplyNoShadingFabricClientMod() {
		this(new SimplyNoShadingClientConfig());
	}

	public SimplyNoShadingFabricClientMod(final boolean createLogger) {
		this(new SimplyNoShadingClientConfig(), createLogger);
	}

	public SimplyNoShadingFabricClientMod(final SimplyNoShadingClientConfig config) {
		this(config, false);
	}

	public SimplyNoShadingFabricClientMod(final SimplyNoShadingClientConfig config, final boolean createLogger) {
		super(config, SimplyNoShadingFabricClientMod::createInitializer, createLogger);

		this.unified = unified();
		this.blockShadingKey = this.unified.constant(() -> toggleKeyMapping("simply-no-shading.key.block_shading",
				getSimplyNoShadingKeyCategory(), getConfig()::shouldShadeBlocks));
		this.openSettingsKey = this.unified
				.constant(() -> keyMapping("simply-no-shading.key.open_settings", getSimplyNoShadingKeyCategory()));
		this.shadingKey = this.unified.constant(() -> toggleKeyMapping("simply-no-shading.key.block_shading", KEY_F6,
				getSimplyNoShadingKeyCategory(), getConfig()::shouldShade));
	}

	public String getSimplyNoShadingKeyCategory() {
		return "simply-no-shading.key.categories.simply-no-shading";
	}

	@Override
	public void loadConfig() {
		getConfig().set(loadConfigInternally());
	}
}

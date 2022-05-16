package com.github.startsmercury.simply.no.shading.entrypoint;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingUtils.GSON;
import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingUtils.runWhenLoaded;
import static com.mojang.blaze3d.platform.InputConstants.KEY_F6;
import static com.mojang.blaze3d.platform.InputConstants.UNKNOWN;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.gui.ShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.CycleOption;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.network.chat.TranslatableComponent;

@Environment(CLIENT)
public final class SimplyNoShadingClientMod implements ClientModInitializer {
	public static final String CATEGORIES_SIMPLY_NO_SHADING = "simply-no-shading.key.categories.simply-no-shading";

	private static SimplyNoShadingClientMod instance;

	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading+client");

	protected static void consumeClick(final KeyMapping keyMapping, final Minecraft client,
	    final Consumer<Minecraft> action) {
		if (keyMapping.consumeClick()) {
			action.accept(client);
		}
	}

	protected static boolean consumeClickZ(final KeyMapping keyMapping, final BooleanSupplier action) {
		if (keyMapping.consumeClick()) {
			return action.getAsBoolean();
		}

		return false;
	}

	public static SimplyNoShadingClientMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("simply-no-shading+client is not yet initialized");
		}

		return instance;
	}

	protected static void registerWhenLoaded(final String id, final KeyMapping keyMapping) {
		runWhenLoaded(id, () -> KeyBindingHelper.registerKeyBinding(keyMapping));
	}

	public final CycleOption<Boolean> allShadingOption;

	public final CycleOption<Boolean> blockShadingOption;

	public final CycleOption<Boolean> cloudShadingOption;

	public final SimplyNoShadingClientConfig config;

	public final Path configPath;

	public final CycleOption<Boolean> enhancedBlockEntityShadingOption;

	public final KeyMapping openSettingsKey;

	public final ToggleKeyMapping toggleAllShadingKey;

	public final ToggleKeyMapping toggleBlockShadingKey;

	public final ToggleKeyMapping toggleCloudShadingKey;

	public final ToggleKeyMapping toggleEnhancedBlockEntityShadingKey;

	public SimplyNoShadingClientMod() {
		LOGGER.debug("Constructing client mod...");

		this.config = new SimplyNoShadingClientConfig();
		this.configPath = FabricLoader.getInstance().getConfigDir().resolve("simply-no-shading+client.json");

		this.openSettingsKey = new KeyMapping("simply-no-shading.key.openSettings", UNKNOWN.getValue(),
		    CATEGORIES_SIMPLY_NO_SHADING);
		this.toggleAllShadingKey = new ToggleKeyMapping("simply-no-shading.key.toggleAllShading", KEY_F6,
		    CATEGORIES_SIMPLY_NO_SHADING, this.config::shouldShadeAll);
		this.toggleBlockShadingKey = new ToggleKeyMapping("simply-no-shading.key.toggleBlockShading",
		    UNKNOWN.getValue(), CATEGORIES_SIMPLY_NO_SHADING, this.config::shouldShadeBlocks);
		this.toggleCloudShadingKey = new ToggleKeyMapping("simply-no-shading.key.toggleCloudShading",
		    UNKNOWN.getValue(), CATEGORIES_SIMPLY_NO_SHADING, this.config::shouldShadeClouds);
		this.toggleEnhancedBlockEntityShadingKey = new ToggleKeyMapping(
		    "simply-no-shading.key.toggleEnhancedBlockEntityShading", UNKNOWN.getValue(), CATEGORIES_SIMPLY_NO_SHADING,
		    this.config::shouldShadeEnhancedBlockEntities);

		this.allShadingOption = CycleOption.createOnOff("simply-no-shading.options.allShading",
		    new TranslatableComponent("simply-no-shading.options.allShading.tooltip"),
		    options -> this.config.shouldShadeAll(),
		    (options, option, allShading) -> this.config.setShadeAll(allShading));
		this.blockShadingOption = CycleOption.createOnOff("simply-no-shading.options.blockShading",
		    new TranslatableComponent("simply-no-shading.options.blockShading.tooltip"),
		    options -> this.config.shouldShadeBlocks(),
		    (options, option, blockShading) -> this.config.setShadeBlocks(blockShading));
		this.cloudShadingOption = CycleOption.createOnOff("simply-no-shading.options.cloudShading",
		    new TranslatableComponent("simply-no-shading.options.cloudShading.tooltip"),
		    options -> this.config.shouldShadeClouds(),
		    (options, option, blockShading) -> this.config.setShadeClouds(blockShading));
		this.enhancedBlockEntityShadingOption = CycleOption.createOnOff(
		    "simply-no-shading.options.enhancedBlockEntityShading",
		    new TranslatableComponent("simply-no-shading.options.enhancedBlockEntityShading.tooltip"),
		    options -> this.config.shouldShadeEnhancedBlockEntities(), (options, option,
		        enhancedBlockEntityShading) -> this.config.setShadeEnhancedBlockEntities(enhancedBlockEntityShading));

		LOGGER.info("Constructed client mod...");
	}

	public void createConfig() {
		try (final var out = Files.newBufferedWriter(this.configPath)) {
			LOGGER.debug("Creating config...");

			GSON.toJson(this.config, out);

			LOGGER.info("Created config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to create config", ioe);
		}
	}

	public void loadConfig() {
		try (final var in = Files.newBufferedReader(this.configPath)) {
			LOGGER.debug("Loading client config...");

			this.config.set(GSON.fromJson(in, SimplyNoShadingClientConfig.class));

			LOGGER.info("Loaded client config");
		} catch (final NoSuchFileException nsfe) {
			createConfig();
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to load config", ioe);
		}
	}

	@Override
	public void onInitializeClient() {
		LOGGER.debug("Initializing client mod...");

		loadConfig();

		runWhenLoaded("fabric-key-binding-api-v1", this::registerKeyMappings, LOGGER,
		    "Unable to register key mappings as the mod provided by 'fabric' (specifically 'fabric-key-binding-api-v1') is not present");
		runWhenLoaded("fabric-lifecycle-events-v1", this::registerLifecycleEventListeners, LOGGER,
		    "Unable to register life cycle event listeners as the mod provided by 'fabric' (specifically 'fabric-lifecycle-events-v1') is not present",
		    this::registerShutdownHook);

		instance = this;

		LOGGER.info("Initialized client mod");
	}

	protected void openSettings(final Minecraft client) {
		if (!(client.screen instanceof ShadingSettingsScreen)) {
			client.setScreen(new ShadingSettingsScreen(client.screen));
		}
	}

	protected void registerKeyMappings() {
		LOGGER.debug("Registering key mappings...");

		KeyBindingHelper.registerKeyBinding(this.openSettingsKey);
		KeyBindingHelper.registerKeyBinding(this.toggleAllShadingKey);
		KeyBindingHelper.registerKeyBinding(this.toggleBlockShadingKey);
		KeyBindingHelper.registerKeyBinding(this.toggleCloudShadingKey);
		registerWhenLoaded("enhancedblockentities", this.toggleEnhancedBlockEntityShadingKey);

		LOGGER.info("Registered key mappings");
	}

	protected void registerLifecycleEventListeners() {
		LOGGER.debug("Registering life cycle event listeners...");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			final var allShadingChanged = consumeClickZ(this.toggleAllShadingKey, this::toggleAllShading);
			final var blockShadingChanged = consumeClickZ(this.toggleBlockShadingKey, this::toggleBlockShading);
			final var cloudShadingChanged = consumeClickZ(this.toggleCloudShadingKey, this::toggleCloudShading);
			final var enhancedBlockEntityShadingChanged = consumeClickZ(this.toggleEnhancedBlockEntityShadingKey,
			    this::toggleEnhancedBlockEntityShading);

			if (cloudShadingChanged && client.levelRenderer instanceof final CloudRenderer cloudRenderer) {
				cloudRenderer.generateClouds();
			}

			if (allShadingChanged || blockShadingChanged || enhancedBlockEntityShadingChanged) {
				client.levelRenderer.allChanged();
			}

			consumeClick(this.openSettingsKey, client, this::openSettings);
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> saveConfig());

		LOGGER.info("Registered life cycle event listeners");
	}

	protected void registerShutdownHook() {
		LOGGER.debug("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));

		LOGGER.info("Registered shutdown hook");
	}

	public void saveConfig() {
		try (final var out = Files.newBufferedWriter(this.configPath)) {
			LOGGER.debug("Saving config...");

			GSON.toJson(this.config, out);

			LOGGER.info("Saved config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to save config", ioe);
		}
	}

	public boolean toggleAllShading() {
		final var wouldHaveShadeAll = this.config.wouldShadeAll();

		this.config.toggleAllShading();

		LOGGER.debug("Toggled shading, the new value is " + this.config.shouldShadeAll());

		return this.config.wouldShadeAll() != wouldHaveShadeAll;
	}

	public boolean toggleBlockShading() {
		final var wouldHaveShadeBlocks = this.config.wouldShadeBlocks();

		this.config.toggleBlockShading();

		LOGGER.debug("Toggled block shading, the new value is " + this.config.shouldShadeBlocks());

		return this.config.wouldShadeBlocks() != wouldHaveShadeBlocks;
	}

	public boolean toggleCloudShading() {
		final var wouldHaveShadeClouds = this.config.wouldShadeClouds();

		this.config.toggleBlockShading();

		LOGGER.debug("Toggled cloud shading, the new value is " + this.config.shouldShadeClouds());

		return this.config.wouldShadeClouds() != wouldHaveShadeClouds;
	}

	public boolean toggleEnhancedBlockEntityShading() {
		final var wouldHaveShadeEnhancedBlockEntities = this.config.wouldShadeEnhancedBlockEntities();

		this.config.toggleEnhancedBlockEntityShading();

		LOGGER.debug("Toggled block shading, the new value is " + this.config.shouldShadeEnhancedBlockEntities());

		return this.config.wouldShadeEnhancedBlockEntities() != wouldHaveShadeEnhancedBlockEntities;
	}
}

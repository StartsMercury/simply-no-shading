package com.github.startsmercury.simply.no.shading.entrypoint;

import static com.github.startsmercury.simply.no.shading.util.Constants.GSON;
import static com.mojang.blaze3d.platform.InputConstants.UNKNOWN;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

@Environment(CLIENT)
public final class SimplyNoShadingClientMod implements ClientModInitializer {
	public static final String CATEGORIES_SIMPLY_NO_SHADING = "simply-no-shading.key.categories.simply-no-shading";

	private static SimplyNoShadingClientMod instance;

	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading+client");

	public static SimplyNoShadingClientMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("simply-no-shading+client is not yet initialized");
		}

		return instance;
	}

	public final SimplyNoShadingClientConfig config;

	public final Path configPath;

	private final FabricLoader fabricLoader;

	public final KeyMapping openSettingsKey;

	public final ToggleKeyMapping toggleBlockShadingKey;

	public final ToggleKeyMapping toggleShadingKey;

	public SimplyNoShadingClientMod() {
		LOGGER.debug("Constructing client mod...");

		this.config = new SimplyNoShadingClientConfig();
		this.fabricLoader = FabricLoader.getInstance();
		this.openSettingsKey = new KeyMapping("simply-no-shading.key.openSettings", UNKNOWN.getValue(),
		    CATEGORIES_SIMPLY_NO_SHADING);

		this.configPath = this.fabricLoader.getConfigDir().resolve("simply-no-shading+client.json");
		this.toggleBlockShadingKey = new ToggleKeyMapping("simply-no-shading.key.toggleBlockShading",
		    UNKNOWN.getValue(), CATEGORIES_SIMPLY_NO_SHADING, this.config::shouldShadeBlocks);
		this.toggleShadingKey = new ToggleKeyMapping("simply-no-shading.key.toggleShading", UNKNOWN.getValue(),
		    CATEGORIES_SIMPLY_NO_SHADING, this.config::shouldShade);

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

		whenModLoaded("fabric-key-bindings-api-v1", this::registerKeyMappings,
		    "Unable to register key mappings as the mod provided by 'fabric' (specifically 'fabric-key-bindings-api-v1') is not present");

		instance = this;

		LOGGER.info("Initialized client mod");
	}

	protected void registerKeyMappings() {
		LOGGER.debug("Registering key mappings...");

		KeyBindingHelper.registerKeyBinding(this.openSettingsKey);
		KeyBindingHelper.registerKeyBinding(this.toggleBlockShadingKey);
		KeyBindingHelper.registerKeyBinding(this.toggleShadingKey);

		LOGGER.info("Registered key mappings");
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

	protected void whenModLoaded(final String id, final Runnable action, final String message) {
		if (this.fabricLoader.isModLoaded(id)) {
			action.run();
		} else {
			LOGGER.warn(message);
		}
	}
}

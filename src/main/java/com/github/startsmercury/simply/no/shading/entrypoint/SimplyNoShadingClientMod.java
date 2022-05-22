package com.github.startsmercury.simply.no.shading.entrypoint;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.GSON;
import static java.nio.file.Files.newBufferedWriter;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.gui.ShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingKeyManager;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

@Environment(CLIENT)
public abstract class SimplyNoShadingClientMod<C extends SimplyNoShadingClientConfig, K extends SimplyNoShadingKeyManager> {
	private static SimplyNoShadingClientMod<?, ?> instance;

	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading/client");

	public static SimplyNoShadingClientMod<?, ?> getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed too early!");
		}

		return instance;
	}

	public final C config;

	public final Path configPath;

	public final K keyManager;

	protected SimplyNoShadingClientMod(final C config, final Path configPath,
	    final Function<? super C, ? extends K> keyManagerProvider) {
		this.config = config;
		this.configPath = configPath;
		this.keyManager = keyManagerProvider.apply(this.config);

		instance = this;
	}

	public void createConfig() {
		LOGGER.debug("Creating config...");

		try (final var out = newBufferedWriter(this.configPath)) {
			GSON.toJson(this.config, out);

			LOGGER.info("Created config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to create config", ioe);
		}
	}

	private Screen createSettingsScreen(final Minecraft client) {
		LOGGER.debug("Creating settings screen...");

		final var settingsScreen = createSettingsScreen(client.screen, this.config);

		LOGGER.info("Created settings screen");
		return settingsScreen;
	}

	protected Screen createSettingsScreen(final Screen screen, final SimplyNoShadingClientConfig config) {
		return new ShadingSettingsScreen(screen, this.config);
	}

	public void loadConfig() {
		try (final var in = Files.newBufferedReader(this.configPath)) {
			LOGGER.debug("Loading config...");

			this.config.copyFrom(GSON.fromJson(in, this.config.getClass()));

			LOGGER.info("Loaded config");
		} catch (final NoSuchFileException nsfe) {
			createConfig();
		} catch (final IOException ioe) {
			LOGGER.debug("Loading config...");

			LOGGER.warn("Unable to load config", ioe);
		}
	}

	protected void openSettingsScreen(final Minecraft client) {
		LOGGER.debug("Opening settings screen...");

		if (!(client.screen instanceof ShadingSettingsScreen)) {
			final var settingsScreen = createSettingsScreen(client);

			client.setScreen(settingsScreen);

			LOGGER.info("Opened settings screen");
		} else {
			LOGGER.warn("Unable to open settings screen as it's already open!");
		}
	}

	protected void registerShutdownHook() {
		LOGGER.debug("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));

		LOGGER.info("Registered shutdown hook");
	}

	public void saveConfig() {
		LOGGER.debug("Saving config...");

		try (final var out = newBufferedWriter(this.configPath)) {
			GSON.toJson(this.config, out);

			LOGGER.info("Saved config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to save config", ioe);
		}
	}
}

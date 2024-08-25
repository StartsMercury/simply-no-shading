package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.gui.screens.ConfigScreen;
import io.github.startsmercury.simply_no_shading.impl.util.JsonUtils;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimplyNoShadingImpl implements SimplyNoShading {
    public static final String MODID = "simply-no-shading";
    public static final String EXPERIMENTAL_ENTITY_SHADING_ID = "simply_no_entity_like_shading";

    public static final Logger LOGGER = LoggerFactory.getLogger("Simply No Shading");
    public static final String KEY_CATEGORY = MODID + ".key.categories." + MODID;

    private static SimplyNoShadingImpl instance;
    private static Path configPath;

    public static void init() {
        LOGGER.debug("Initializing Simply No Shading...");

        if (instance != null) {
            LOGGER.warn("Simply No Shading is already initialized!");
            return;
        }

        final var fabricLoader = FabricLoader.getInstance();
        configPath = fabricLoader.getConfigDir().resolve(MODID + ".json");

        final var self = new SimplyNoShadingImpl();
        self.loadConfig();
        self.registerKeyMappings(fabricLoader);
        self.registerResources(fabricLoader);
        self.registerShutdownHook();
        instance = self;

        LOGGER.info("Simply No Shading is initialized.");
    }

    public static @NotNull SimplyNoShading instance() {
        if (instance != null) {
            return instance;
        } else {
            throw new RuntimeException("Simply No Shading is not yet initialized");
        }
    }

    private final ConfigImpl config;
    private final KeyMapping keyOpenConfigScreen;
    private final KeyMapping keyReloadConfig;
    private final List<KeyMapping> keyShadingToggles;

    private SimplyNoShadingImpl() {
        this.config = new ConfigImpl();
        this.keyOpenConfigScreen = SimplyNoShadingImpl.createKeyMapping("openConfigScreen");
        this.keyReloadConfig = SimplyNoShadingImpl.createKeyMapping("reloadConfig");
        this.keyShadingToggles = ShadingTarget
            .valueList()
            .stream()
            .map(ShadingTarget::toggleKey)
            .map(SimplyNoShadingImpl::createKeyMapping)
            .toList();
    }

    @Override
    public @NotNull Path configPath() {
        assert configPath != null : "This should have been initialized before this, the instance";
        return configPath;
    }

    @Override
    public @NotNull Config config() {
        return new ConfigImpl(this.config);
    }

    @Override
    public void setConfig(final @NotNull Config config) {
        this.config.set(config);
        ComputedConfig.set(config);
    }

    public void loadConfig() {
        LOGGER.debug("[Simply No Shading] Loading the config...");

        try {
            final var reader = Files.newBufferedReader(this.configPath());
            this.loadConfigHelper(reader);
        } catch (final NoSuchFileException cause) {
            LOGGER.info("[Simply No Shading] Config file is not present, defaults will be used.");
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while creating the config file reader.",
                cause
            );
        }
    }

    private void loadConfigHelper(final Reader reader) {
        try (reader) {
            final var json = JsonParser.parseReader(reader);

            final var config = new ConfigImpl(this.config);
            config.fromJson(json);
            this.setConfig(config);

            LOGGER.info("[Simply No Shading] The config is loaded.");
        } catch (final JsonParseException cause) {
            LOGGER.error(
                "[Simply No Shading] An exception occurred while parsing the config.",
                cause
            );
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while closing the config file reader.",
                cause
            );
        }
    }

    public void saveConfig() {
        LOGGER.debug("[Simply No Shading] Saving the config...");

        final var tree = this.parseConfigAsJsonObject();
        this.config.toJson(tree);

        try {
            final var writer = Files.newBufferedWriter(this.configPath());
            this.saveConfigHelper(writer, tree);
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while creating the config file writer.",
                cause
            );
        }
    }

    private JsonObject parseConfigAsJsonObject() {
        try (final var reader = Files.newBufferedReader(this.configPath())) {
            if (JsonParser.parseReader(reader) instanceof JsonObject jsonObject) {
                return jsonObject;
            }
        } catch (final IOException | JsonParseException ignored) {

        }

        return new JsonObject();
    }

    private void saveConfigHelper(final Writer writer, final JsonObject tree) {
        final var jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent("    ");

        try (writer; jsonWriter) {
            this.saveConfigHelperHelper(jsonWriter, tree);
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while closing the config file writer.",
                cause
            );
        }
    }

    private void saveConfigHelperHelper(final JsonWriter jsonWriter, final JsonObject tree) {
        try {
            JsonUtils.serialize(jsonWriter, tree);
            LOGGER.info("[Simply No Shading] The config is saved.");
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while writing to the config file.",
                cause
            );
        }
    }

    public KeyMapping keyOpenConfigScreen() {
        return this.keyOpenConfigScreen;
    }

    public KeyMapping keyReloadConfig() {
        return this.keyReloadConfig;
    }

    public List<? extends KeyMapping> keyShadingToggles() {
        return this.keyShadingToggles;
    }

    private void registerKeyMappings(final FabricLoader fabricLoader) {
        if (
            !fabricLoader.isModLoaded("fabric-key-binding-api-v1")
                || !fabricLoader.isModLoaded("fabric-lifecycle-events-v1")
        ) {
            return;
        }

        KeyBindingHelper.registerKeyBinding(this.keyOpenConfigScreen());
        KeyBindingHelper.registerKeyBinding(this.keyReloadConfig());
        this.keyShadingToggles().forEach(KeyBindingHelper::registerKeyBinding);

        ClientTickEvents.END_CLIENT_TICK.register(this::consumeKeyEvents);
    }

    private static KeyMapping createKeyMapping(final String name) {
        return new KeyMapping(
            "simply-no-shading.key." + name,
            InputConstants.UNKNOWN.getValue(),
            KEY_CATEGORY
        );
    }

    private void consumeKeyEvents(final Minecraft minecraft) {
        if (this.keyOpenConfigScreen().isDown()) {
            final var lastScreen = minecraft.screen;
            final var config = this.config();

            minecraft.setScreen(new ConfigScreen(lastScreen, config));
        } else if (this.keyReloadConfig().isDown()) {
            this.reloadConfig(minecraft);
        } else {
            this.consumeKeyToggleEvents(minecraft);
        }
    }

    private void reloadConfig(final Minecraft minecraft) {
        final var oldShadingValues = this.config.shadingValues.clone();
        this.loadConfig();
        final var newShadingValues = this.config.shadingValues;

        var reloadType = ReloadType.NONE;
        for (final var target : ShadingTarget.valueList()) {
            final var ordinal = target.ordinal();

            final var oldValue = oldShadingValues[ordinal];
            final var newValue = newShadingValues[ordinal];

            if (oldValue != newValue) {
                reloadType = reloadType.compose(target.reloadType());
            }
        }

        reloadType.applyTo(minecraft);
    }

    private void consumeKeyToggleEvents(final Minecraft minecraft) {
        final var shadingValues = this.config.shadingValues.clone();

        var reloadType = ReloadType.NONE;
        for (final var target : ShadingTarget.valueList()) {
            final var ordinal = target.ordinal();
            if (!this.keyShadingToggles.get(ordinal).consumeReleased()) {
                continue;
            }
            shadingValues[ordinal] ^= true;
            reloadType = reloadType.compose(target.reloadType());
        }

        if (reloadType != ReloadType.NONE) {
            this.config.shadingValues = shadingValues;
            ComputedConfig.set(this.config);
            reloadType.applyTo(minecraft);
        }
    }

    private void registerResources(final FabricLoader fabricLoader) {
        registerResourcesButSelfless(fabricLoader);
    }

    private static void registerResourcesButSelfless(final FabricLoader fabricLoader) {
        if (!fabricLoader.isModLoaded("fabric-resource-loader-v0")) {
            return;
        }
        final var container = fabricLoader
            .getModContainer(MODID)
            .orElseThrow(() -> new AssertionError("""
                Fabric mod container for ${MODID} does not exist. Developer might have used the a \
                different mod id from the one in fabric.mod.json. Please create an issue in their \
                repository.\
            """.replace("${MODID}", MODID)));
        final var success = ResourceManagerHelper.registerBuiltinResourcePack(
            new ResourceLocation(MODID, EXPERIMENTAL_ENTITY_SHADING_ID),
            container,
            ResourcePackActivationType.NORMAL
        );
        if (!success) {
            LOGGER.warn(
                "[Simply No Shading] Unable to register the built-in resource pack {}",
                EXPERIMENTAL_ENTITY_SHADING_ID
            );
        }
    }

    private void registerShutdownHook() {
        final var shutdownThread = new Thread(this::saveConfig);
        shutdownThread.setName("Simply No Shading Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}

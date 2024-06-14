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
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplyNoShadingImpl implements SimplyNoShading {
    public static final String MODID = "simply-no-shading";

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
    private final ObjectList<? extends ShadingToggle> shadingToggles;

    public SimplyNoShadingImpl() {
        this.config = new ConfigImpl(false, false);
        this.keyOpenConfigScreen = SimplyNoShadingImpl.createKeyMapping("keyOpenConfigScreen");
        this.keyReloadConfig = SimplyNoShadingImpl.createKeyMapping("keyReloadConfig");
        this.shadingToggles = ObjectList.of(
            createShadingToggle("blockShading", ReloadType.blocks()),
            createShadingToggle("cloudShading", ReloadType.clouds())
        );
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

    public ObjectList<? extends ShadingToggle> shadingToggles() {
        return this.shadingToggles;
    }

    protected void registerKeyMappings(final FabricLoader fabricLoader) {
        if (
            !fabricLoader.isModLoaded("fabric-key-binding-api-v1")
                || !fabricLoader.isModLoaded("fabric-lifecycle-events-v1")
        ) {
            return;
        }

        KeyBindingHelper.registerKeyBinding(this.keyOpenConfigScreen());
        KeyBindingHelper.registerKeyBinding(this.keyReloadConfig());
        for (final var shadingToggle : this.shadingToggles()) {
            KeyBindingHelper.registerKeyBinding(shadingToggle.keyMapping());
        }

        ClientTickEvents.END_CLIENT_TICK.register(this::consumeKeyEvents);
    }

    private static KeyMapping createKeyMapping(final String name) {
        return new KeyMapping(
            "simply-no-shading.key." + name,
            InputConstants.UNKNOWN.getValue(),
            KEY_CATEGORY
        );
    }

    private static ShadingToggle createShadingToggle(
        final String name,
        final ReloadType reloadType
    ) {
        return new ShadingToggle(
            name,
            false,
            createKeyMapping("toggle" + toTitleCase(name)),
            reloadType
        );
    }

    private static String toTitleCase(final String s) {
        return switch (s.length()) {
            case 0 -> "";
            case 1 -> String.valueOf(Character.toTitleCase(s.charAt(0)));
            default -> Character.toTitleCase(s.charAt(0)) + s.substring(1);
        };
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
        final var oldShadingValues = new Reference2BooleanOpenHashMap<>(this.config.shadingValues);
        this.loadConfig();
        final var newShadingValues = this.config.shadingValues;

        var reloadType = ReloadType.NONE;
        for (final var shadingToggle : this.shadingToggles()) {
            final var name = shadingToggle.name();
            final var defaultValue = shadingToggle.defaultValue();

            final var oldValue = oldShadingValues.getOrDefault(name, defaultValue);
            final var newValue = newShadingValues.getOrDefault(name, defaultValue);

            if (oldValue != newValue) {
                reloadType = reloadType.compose(shadingToggle.reloadType());
            }
        }

        System.out.println(reloadType);
        if (reloadType != ReloadType.NONE) {
            reloadType.applyTo(minecraft.levelRenderer);
        }
    }

    private void consumeKeyToggleEvents(final Minecraft minecraft) {
        final var config = new ConfigImpl(this.config());

        var reloadType = ReloadType.NONE;
        for (final var shadingToggle : this.shadingToggles()) {
            reloadType = reloadType.compose(shadingToggle.tryToggleOnKeyRelease(config));
        }

        if (reloadType != ReloadType.NONE) {
            reloadType.applyTo(minecraft.levelRenderer);
            this.setConfig(config);
        }
    }

    protected void registerResources(final FabricLoader fabricLoader) {
        if (!fabricLoader.isModLoaded("fabric-resource-loader-v0")) {
            return;
        }
        fabricLoader
            .getModContainer(MODID)
            .ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(
                ResourceLocation.fromNamespaceAndPath(MODID, "simply_no_entity_like_shading"),
                container,
                Component.literal("Entity(ish) No Shading"),
                ResourcePackActivationType.NORMAL
            ));
    }

    private void registerShutdownHook() {
        final var shutdownThread = new Thread(this::saveConfig);
        shutdownThread.setName("Simply No Shading Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}

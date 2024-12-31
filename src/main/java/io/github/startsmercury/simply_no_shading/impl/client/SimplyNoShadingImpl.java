package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.gui.screens.ConfigScreen;
import java.io.*;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public final class SimplyNoShadingImpl implements SimplyNoShading {
    public static final String NAME = "Simply No Shading";
    public static final String MODID = "simply-no-shading";

    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final String KEY_CATEGORY = MODID + ".key.categories." + MODID;

    private static SimplyNoShadingImpl instance;
    private static Path configPath;

    public static void init() {
        LOGGER.debug("Initializing {}...", NAME);

        if (instance != null) {
            LOGGER.warn("{} is already initialized!", NAME);
            return;
        }

        final FabricLoader fabricLoader = FabricLoader.getInstance();
        configPath = fabricLoader.getConfigDir().resolve(MODID + ".json");

        SimplyNoShadingImpl.instance = new SimplyNoShadingImpl(fabricLoader);

        LOGGER.info("{} is initialized.", NAME);
    }

    public static @NotNull SimplyNoShadingImpl instance() {
        if (instance != null) {
            return instance;
        } else {
            throw new RuntimeException(NAME + " is not yet initialized");
        }
    }

    private final ConfigImpl config;
    private final GameContext context;
    private SoftReference<Gson> gsonRef;
    private final KeyMapping keyOpenConfigScreen;
    private final KeyMapping keyReloadConfig;
    private final List<KeyMapping> keyShadingToggles;
    private boolean lightingForceOff;

    private SimplyNoShadingImpl(FabricLoader fabricLoader) {
        this.config = new ConfigImpl();
        this.context = new GameContext();
        this.gsonRef = new SoftReference<>(null);
        this.keyOpenConfigScreen = SimplyNoShadingImpl.createKeyMapping("open_config_screen");
        this.keyReloadConfig = SimplyNoShadingImpl.createKeyMapping("reload_config");
        this.keyShadingToggles = ShadingTarget
            .valueList()
            .stream()
            .map(ShadingTarget::toggleKey)
            .map(SimplyNoShadingImpl::createKeyMapping)
            .collect(Collectors.toList());

        this.loadConfig();
        this.registerKeyMappings(fabricLoader);
        this.registerShutdownHook();

        if (fabricLoader.isModLoaded("sodium")) {
            this.context.setSodiumLoaded(true);
        }
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

    public GameContext context() {
        return this.context;
    }

    private Gson gson() {
        Gson gson = this.gsonRef.get();
        if (gson == null) {
            this.gsonRef = new SoftReference<>(gson = new Gson());
        }
        return gson;
    }

    public void loadConfig() {
        LOGGER.debug("[{}] Loading the config...", NAME);

        try {
            final BufferedReader reader = Files.newBufferedReader(this.configPath());
            this.loadConfigHelper(reader);
        } catch (final NoSuchFileException cause) {
            LOGGER.info("[{}] Config file not present, defaults will be used.", NAME);
        } catch (final IOException cause) {
            LOGGER.error("[{}] Unable to create config file reader.", NAME, cause);
        }
    }

    private void loadConfigHelper(final Reader reader) {
        try {
            final Config config = this.gson().fromJson(reader, ConfigImpl.class);
            this.setConfig(config);
            LOGGER.info("[{}] The config is loaded.", NAME);
        } catch (final JsonSyntaxException cause) {
            LOGGER.error("[{}] Invalid config JSON syntax.", NAME, cause);
        } catch (final JsonIOException cause) {
            LOGGER.error("[{}] Unable to read config JSON.", NAME, cause);
        } finally {
            try {
                reader.close();
            } catch (final IOException cause) {
                LOGGER.error("[{}] Unable to soundly close config file reader.", NAME, cause);
            }
        }
    }

    public void saveConfig() {
        LOGGER.debug("[{}] Saving the config...", NAME);
        final Gson gson = this.gson();

        final JsonObject tree = this.parseConfigAsJsonObject();
        final JsonElement overrides = gson.toJsonTree(this.config());
        if (overrides instanceof JsonObject) {
            for (final Map.Entry<String, JsonElement> entry : ((JsonObject) overrides).entrySet()) {
                tree.add(entry.getKey(), entry.getValue());
            }
        } else {
            throw new AssertionError("Expected config to serialize as JSON object");
        }

        try {
            final BufferedWriter writer = Files.newBufferedWriter(this.configPath());
            this.saveConfigHelper(gson, tree, writer);
        } catch (final IOException cause) {
            LOGGER.error("[{}] Unable to create config file writer.", NAME, cause);
        }
    }

    private JsonObject parseConfigAsJsonObject() {
        try (final BufferedReader reader = Files.newBufferedReader(this.configPath())) {
            final JsonElement json = new JsonParser().parse(reader);
            if (json instanceof JsonObject) {
                return (JsonObject) json;
            }
        } catch (final IOException | JsonParseException ignored) {

        }

        return new JsonObject();
    }

    private void saveConfigHelper(
        final Gson gson,
        final JsonObject tree,
        final Writer writer
    ) {
        final JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent("    ");

        try {
            this.saveConfigHelperHelper(gson, tree, jsonWriter);
        } finally {
            try {
                jsonWriter.close();
            } catch (final IOException cause) {
                LOGGER.error("[{}] Unable to soundly close config file writer.", NAME, cause);
            }
        }
    }

    private void saveConfigHelperHelper(
        final Gson gson,
        final JsonObject tree,
        final JsonWriter jsonWriter
    ) {
        try {
            gson.toJson(tree, jsonWriter);
            LOGGER.info("[{}] The config is saved.", NAME);
        } catch (final JsonIOException cause) {
            LOGGER.error("[{}] Unable to write to config file.", NAME, cause);
        }
    }

    public FabricKeyBinding keyOpenConfigScreen() {
        return this.keyOpenConfigScreen;
    }

    public FabricKeyBinding keyReloadConfig() {
        return this.keyReloadConfig;
    }

    public List<? extends KeyMapping> keyShadingToggles() {
        return this.keyShadingToggles;
    }

    public boolean isLightingForceOff() {
        return this.lightingForceOff;
    }

    public void lightingScope(final Runnable action) {
        if (this.config().entityShadingEnabled()) {
            action.run();
        } else {
            Lighting.turnOff();
            this.lightingForceOff = true;
            action.run();
            this.lightingForceOff = false;
        }
    }

    private void registerKeyMappings(final FabricLoader fabricLoader) {
        if (
            !fabricLoader.isModLoaded("fabric-keybindings-v0")
                || !fabricLoader.isModLoaded("fabric-events-lifecycle-v0")
        ) {
            return;
        }

        KeyBindingRegistry.INSTANCE.register(this.keyOpenConfigScreen());
        KeyBindingRegistry.INSTANCE.register(this.keyReloadConfig());
        this.keyShadingToggles().forEach(KeyBindingRegistry.INSTANCE::register);

        ClientTickCallback.EVENT.register(this::consumeKeyEvents);
    }

    private static KeyMapping createKeyMapping(final String name) {
        return new KeyMapping(
            new ResourceLocation("simply-no-shading", name),
            InputConstants.UNKNOWN.getValue(),
            KEY_CATEGORY
        );
    }

    private void consumeKeyEvents(final Minecraft minecraft) {
        if (this.keyOpenConfigScreen().isDown()) {
            final Screen lastScreen = minecraft.screen;
            final Config config = this.config();

            minecraft.setScreen(new ConfigScreen(lastScreen, config));
        } else if (this.keyReloadConfig().isDown()) {
            this.reloadConfig(minecraft);
        } else {
            this.consumeKeyToggleEvents(minecraft);
        }
    }

    public void applyChangesBetween(final Config lhs, final Config rhs, final Minecraft minecraft) {
        final GameContext context = this.context();

        ShadingTarget.valueList()
            .stream()
            .filter(target -> target.changedBetween(lhs, rhs))
            .map(target -> target.reloadTypeFor(context))
            .max(Comparator.naturalOrder())
            .orElse(ReloadLevel.NONE)
            .applyTo(minecraft);
    }

    private void reloadConfig(final Minecraft minecraft) {
        final Config oldConfig = this.config();
        this.loadConfig();
        final Config newConfig = this.config();

        this.applyChangesBetween(oldConfig, newConfig, minecraft);
    }

    private void consumeKeyToggleEvents(final Minecraft minecraft) {
        final GameContext context = this.context();

        if (context().shadersEnabled()) {
            this.keyShadingToggles().forEach(KeyMapping::consumeAction);
            return;
        }

        final Config config = this.config();
        final List<KeyMapping> keyShadingToggles = this.keyShadingToggles;

        final ReloadLevel reloadType = ShadingTarget.valueList()
            .stream()
            .filter(target -> keyShadingToggles.get(target.ordinal()).consumeReleased())
            .peek(target -> target.setInto(config, !target.getFrom(config)))
            .map(target -> target.reloadTypeFor(context))
            .max(Comparator.naturalOrder())
            .orElse(null);

        if (reloadType != null) {
            this.setConfig(config);
            ComputedConfig.set(config);
            reloadType.applyTo(minecraft);
        }
    }

    private void registerShutdownHook() {
        final Thread shutdownThread = new Thread(this::saveConfig);
        shutdownThread.setName(NAME + " Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}

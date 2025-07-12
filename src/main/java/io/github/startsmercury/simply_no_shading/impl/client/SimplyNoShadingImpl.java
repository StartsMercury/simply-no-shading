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
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public final class SimplyNoShadingImpl implements SimplyNoShading {
    private final ConfigImpl config;
    private final Path configPath;
    private final GameContext context;
    private final FabricLoader fabricLoader;
    private SoftReference<Gson> gsonRef;
    private final KeyMapping keyOpenConfigScreen;
    private final KeyMapping keyReloadConfig;
    private final List<KeyMapping> keyShadingToggles;
    private final Logger logger;
    private final Minecraft minecraft;
    private boolean lightingForceOff;

    public SimplyNoShadingImpl(final Minecraft minecraft) {
        this.config = new ConfigImpl();
        this.context = new GameContext();
        this.fabricLoader = FabricLoader.getInstance();
        this.gsonRef = new SoftReference<>(null);
        this.keyOpenConfigScreen = SimplyNoShadingImpl.createKeyMapping("openConfigScreen");
        this.keyReloadConfig = SimplyNoShadingImpl.createKeyMapping("reloadConfig");
        this.keyShadingToggles = ShadingTarget
            .valueList()
            .stream()
            .map(ShadingTarget::toggleKey)
            .map(SimplyNoShadingImpl::createKeyMapping)
            .collect(Collectors.toList());
        this.logger = LogManager.getLogger(SnsConstants.NAME);
        this.minecraft = minecraft;

        this.configPath = this.fabricLoader.getConfigDir().resolve(SnsConstants.MODID + ".json");
    }

    public void onInitialize() {
        this.logger.debug("Initializing {}...", SnsConstants.NAME);

        this.loadConfig();
        this.registerKeyMappings();
        this.registerShutdownHook();

        if (this.fabricLoader.isModLoaded("sodium")) {
            this.context.setSodiumLoaded(true);
        }

        this.logger.info("{} is initialized.", SnsConstants.NAME);
    }

    @Override
    public @NotNull Path configPath() {
        return this.configPath;
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
        this.logger.debug("[{}] Loading the config...", SnsConstants.NAME);

        try {
            final BufferedReader reader = Files.newBufferedReader(this.configPath());
            this.loadConfigHelper(reader);
        } catch (final NoSuchFileException cause) {
            this.logger.info("[{}] Config file not present, defaults will be used.", SnsConstants.NAME);
        } catch (final IOException cause) {
            this.logger.error("[{}] Unable to create config file reader.", SnsConstants.NAME, cause);
        }
    }

    private void loadConfigHelper(final Reader reader) {
        try {
            final Config config = this.gson().fromJson(reader, ConfigImpl.class);
            this.setConfig(config);
            this.logger.info("[{}] The config is loaded.", SnsConstants.NAME);
        } catch (final JsonSyntaxException cause) {
            this.logger.error("[{}] Invalid config JSON syntax.", SnsConstants.NAME, cause);
        } catch (final JsonIOException cause) {
            this.logger.error("[{}] Unable to read config JSON.", SnsConstants.NAME, cause);
        } finally {
            try {
                reader.close();
            } catch (final IOException cause) {
                this.logger.error("[{}] Unable to soundly close config file reader.", SnsConstants.NAME, cause);
            }
        }
    }

    public void saveConfig() {
        this.logger.debug("[{}] Saving the config...", SnsConstants.NAME);
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
            this.logger.error("[{}] Unable to create config file writer.", SnsConstants.NAME, cause);
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
                this.logger.error("[{}] Unable to soundly close config file writer.", SnsConstants.NAME, cause);
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
            this.logger.info("[{}] The config is saved.", SnsConstants.NAME);
        } catch (final JsonIOException cause) {
            this.logger.error("[{}] Unable to write to config file.", SnsConstants.NAME, cause);
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

    private void registerKeyMappings() {
        if (!this.fabricLoader.isModLoaded("fabric-key-binding-api-v1")
            || !this.fabricLoader.isModLoaded("fabric-lifecycle-events-v1")
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
            SnsConstants.KEY_CATEGORY
        );
    }

    private void consumeKeyEvents(final Minecraft ignored) {
        if (this.keyOpenConfigScreen().isDown()) {
            final Screen lastScreen = this.minecraft.screen;

            this.minecraft.setScreen(this.createConfigScreen(lastScreen));
        } else if (this.keyReloadConfig().isDown()) {
            this.reloadConfig();
        } else {
            this.consumeKeyToggleEvents();
        }
    }

    public Screen createConfigScreen(final Screen lastScreen) {
        return new ConfigScreen(lastScreen, this.config(), newConfig -> {
            final Config oldConfig = this.config();
            this.setConfig(newConfig);
            this.saveConfig();
            this.applyChangesBetween(oldConfig, newConfig);
        });
    }

    public void applyChangesBetween(final Config lhs, final Config rhs) {
        final GameContext context = this.context();

        ShadingTarget.valueList()
            .stream()
            .filter(target -> target.changedBetween(lhs, rhs))
            .map(target -> target.reloadTypeFor(context))
            .max(Comparator.naturalOrder())
            .orElse(ReloadLevel.NONE)
            .applyTo(this.minecraft);
    }

    private void reloadConfig() {
        final Config oldConfig = this.config();
        this.loadConfig();
        final Config newConfig = this.config();

        this.applyChangesBetween(oldConfig, newConfig);
    }

    private void consumeKeyToggleEvents() {
        final GameContext context = this.context();

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
            reloadType.applyTo(this.minecraft);
        }
    }

    private void registerShutdownHook() {
        final Thread shutdownThread = new Thread(this::saveConfig);
        shutdownThread.setName(SnsConstants.NAME + " Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}

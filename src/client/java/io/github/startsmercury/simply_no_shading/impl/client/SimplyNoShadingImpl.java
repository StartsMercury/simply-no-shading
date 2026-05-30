package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.startsmercury.simply_no_shading.impl.client.config.IConfig;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigPreset;
import io.github.startsmercury.simply_no_shading.impl.client.gui.screens.ConfigScreen;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimplyNoShadingImpl {
    public static final KeyMapping.Category KEY_MAPPING_CATEGORY = KeyMapping.Category.register(
        Identifier.fromNamespaceAndPath(SnsConstants.MODID, SnsConstants.MODID)
    );

    private static final Config DEFAULT_CONFIG =
        new Config(true, ConfigPreset.VANILLA, Optional.empty());

    public static SimplyNoShadingRenderState mainRenderState() {
        return renderStateOf(Minecraft.getInstance());
    }

    public static SimplyNoShadingRenderState renderStateOf(final Minecraft minecraft) {
        return minecraft.getSimplyNoShading().renderState();
    }

    private final GameContext context;
    private final FabricLoader fabricLoader;
    private final KeyMapping keyOpenModConfig;
    private final KeyMapping keyReloadConfig;
    private final KeyMapping keyToggleBlockShading;
    private final KeyMapping keyToggleCloudShading;
    private final KeyMapping keyToggleEntityShading;
    private final Logger logger;
    private final Minecraft minecraft;
    private final SimplyNoShadingRenderState renderState;

    private Config config = DEFAULT_CONFIG;

    public SimplyNoShadingImpl(final Minecraft minecraft) {
        this.context = new GameContext();
        this.fabricLoader = FabricLoader.getInstance();
        this.keyOpenModConfig = SimplyNoShadingImpl.createKeyMapping("openModConfig");
        this.keyReloadConfig = SimplyNoShadingImpl.createKeyMapping("reloadConfig");
        this.keyToggleBlockShading = SimplyNoShadingImpl.createKeyMapping("toggleBlockShading");
        this.keyToggleCloudShading = SimplyNoShadingImpl.createKeyMapping("toggleCloudShading");
        this.keyToggleEntityShading = SimplyNoShadingImpl.createKeyMapping("toggleEntityShading");
        this.logger = LoggerFactory.getLogger(SnsConstants.NAME);
        this.minecraft = minecraft;
        this.renderState = new SimplyNoShadingRenderState();
    }

    public void onInitialize() {
        this.logger.debug("Initializing {}...", SnsConstants.NAME);

        this.setConfig(this.loadConfig().orElse(Config.DEFAULT));

        this.registerKeyMappings();
        this.registerShutdownHook();

        if (this.fabricLoader.isModLoaded("bedrockify")) {
            this.context.setBedrockifyLoaded(true);
        }

        if (this.fabricLoader.isModLoaded("sodium")) {
            this.context.setSodiumLoaded(true);
        }

        this.logger.info("{} is initialized.", SnsConstants.NAME);
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfigAndReload(final Config config) {
        final var oldConfig = this.setConfig(config);

        final var context = this.getContext();
        final var reloadLevel = getReloadLevel(oldConfig, config, context);

        reloadLevel.applyTo(this.minecraft);
    }

    private Config setConfig(final Config config) {
        final var oldConfig = this.config;
        this.config = config;

        this.renderState.compatibilityMode = config.compatibilityMode();
        final var data = config.data();
        this.renderState.shadeBlocks = data.shadeBlocks();
        this.renderState.shadeClouds = data.shadeClouds();
        this.renderState.shadeEntities = data.shadeEntities();

        return oldConfig;
    }

    private static ReloadLevel getReloadLevel(
        final Config oldConfig,
        final Config newConfig,
        final GameContext context
    ) {
        final ReloadLevel reloadLevel;
        if (oldConfig.data().shadeEntities() != newConfig.data().shadeEntities()) {
            reloadLevel = ReloadLevel.RESOURCE_PACKS;
        } else if (context.isShadersEnabled()) {
            reloadLevel = ReloadLevel.NONE;
        } else if (oldConfig.data().shadeBlocks() != newConfig.data().shadeBlocks()) {
            reloadLevel = ReloadLevel.ALL_CHANGED;
        } else if (oldConfig.data().shadeClouds() != newConfig.data().shadeClouds()) {
            reloadLevel = context.isSodiumLoaded() ? ReloadLevel.ALL_CHANGED : ReloadLevel.NEEDS_UPDATE;
        } else {
            reloadLevel = ReloadLevel.NONE;
        }
        return reloadLevel;
    }

    public GameContext getContext() {
        return this.context;
    }

    public void openConfigFile() {
        this.logger.debug("[{}] Opening config...", SnsConstants.NAME);
        Util.getPlatform().openFile(this.getConfigFile());
    }

    public void reloadConfig() {
        this.loadConfig().ifPresentOrElse(
            config -> {
                this.setConfigAndReload(config);
                this.saveConfig();
            },
            this::openConfigFile
        );
    }

    /**
     * @return {@code false} if loading encountered json syntax exceptions;
     *     {@code true} otherwise.
     */
    public Optional<Config> loadConfig() {
        // TODO, separate unified load and save
        this.logger.debug("[{}] Loading the config...", SnsConstants.NAME);

        final var path = this.getConfigPath();
        final JsonElement json;

        final ArrayList<CharSequence> lines;
        try (final var lineStream = Files.lines(path)) {
            lines = lineStream
                .filter(line -> !line.endsWith(SnsConstants.IGNORE_TAG))
                .collect(Collectors.toCollection(ArrayList::new));
        } catch (final NoSuchFileException cause) {
            this.logger.info("[{}] Config does not exist, using default", SnsConstants.NAME);
            return Optional.of(Config.DEFAULT);
        } catch (final IOException cause) {
            this.logger.warn("[{}] Unable to read config json", SnsConstants.NAME, cause);
            return Optional.of(Config.DEFAULT);
        }

        try{
            json = JsonParser.parseString(String.join("\n", lines));
        } catch (final JsonParseException cause) {
            this.logger.warn("[{}] Invalid config json syntax", SnsConstants.NAME, cause);

            final var lineMatcher = SnsConstants.LINE_PATTERN.matcher(cause.getMessage());
            var line = 0;

            if (lineMatcher.find()) {
                final var capturedLine = lineMatcher.group(1);
                try {
                    line = Integer.parseInt(capturedLine);
                } catch (final NumberFormatException ignored) {

                }
            }

            final var errorMessageBuilder = new StringBuilder();

            final var columnMatcher = SnsConstants.COLUMN_PATTERN.matcher(cause.getMessage());

            if (columnMatcher.find()) {
                try {
                    final var capturedColumn = columnMatcher.group(1);
                    final var column = Integer.parseInt(capturedColumn);

                    if (column >= 2) {
                        errorMessageBuilder.append(" ".repeat(column - 2));
                    }

                    errorMessageBuilder.append("^ ");
                } catch (final NumberFormatException ignored) {

                }
            }

            errorMessageBuilder.append(cause.getMessage())
                .append("\t")
                .append(SnsConstants.IGNORE_TAG);
            lines.add(line, errorMessageBuilder);

            try {
                Files.write(path, lines);
            } catch (final IOException cause2) {
                this.logger.warn(
                    "[{}] Unable to update config json with an error message",
                    SnsConstants.NAME,
                    cause2
                );
            }

            return Optional.empty();
        }

        if (json instanceof final JsonObject object && !object.has("version")) {
            object.addProperty("version", IConfig.MIN_VERSION);
        }

        switch (
            IConfig.LENIENT_CODEC
                .decode(JsonOps.INSTANCE, json)
                .map(Pair::getFirst)
                .map(IConfig::upgrade)
        ) {
            case DataResult.Success(final DataResult.Success<Config> success, _) -> {
                return Optional.of(success.value());
            }
            case DataResult.Success(final DataResult.Error<?> error, _) -> {
                if (this.logger.isWarnEnabled()) {
                    this.logger.atWarn().log(() ->
                        "[" + SnsConstants.NAME + "] Unable to upgrade config: " + error.message()
                    );
                }
            }
            case final DataResult.Error<?> error -> {
                if (this.logger.isWarnEnabled()) {
                    this.logger.atWarn().log(() ->
                        "[" + SnsConstants.NAME + "] Unable to decode config: " + error.message()
                    );
                }
            }
        }

        return Optional.empty();
    }

    public void saveConfig() {
        this.logger.debug("[{}] Saving config...", SnsConstants.NAME);

        final var path = this.fabricLoader.getConfigDir().resolve(SnsConstants.CONFIG_NAME);

        final JsonObject json;
        switch (IConfig.CODEC.encodeStart(JsonOps.INSTANCE, this.config)) {
            case final DataResult.Success<JsonElement> success:
                json = (JsonObject) success.value();
                break;
            case final DataResult.Error<?> error:
                if (this.logger.isWarnEnabled()) {
                    this.logger.atWarn().log(() ->
                        "[" + SnsConstants.NAME + "] Unable to encode config: " + error.message()
                    );
                }
                return;
        }

        // json.addProperty("__message", "Click the config button again to load changes.");

        try (
            final var bufferedWriter = Files.newBufferedWriter(path);
            final var jsonWriter = new JsonWriter(bufferedWriter)
        ) {
            jsonWriter.setIndent("    ");

            GsonHelper.writeValue(jsonWriter, json, Comparator.naturalOrder());

            bufferedWriter.newLine();
        } catch (final IOException cause) {
            this.logger.warn("[{}] Unable to write config json", SnsConstants.NAME, cause);
        }
    }

    private Path getConfigPath() {
        return this.fabricLoader.getConfigDir().resolve(SnsConstants.CONFIG_NAME);
    }

    public File getConfigFile() {
        return this.getConfigPath().toFile();
    }

    public KeyMapping keyOpenModConfig() {
        return this.keyOpenModConfig;
    }

    public KeyMapping keyReloadConfig() {
        return this.keyReloadConfig;
    }

    public KeyMapping keyToggleBlockShading() {
        return this.keyToggleBlockShading;
    }

    public KeyMapping keyToggleCloudShading() {
        return this.keyToggleCloudShading;
    }

    public KeyMapping keyToggleEntityShading() {
        return this.keyToggleEntityShading;
    }

    private void registerKeyMappings() {
        if (!this.fabricLoader.isModLoaded("fabric-key-mapping-api-v1")
            || !this.fabricLoader.isModLoaded("fabric-lifecycle-events-v1")
        ) {
            return;
        }

        KeyMappingHelper.registerKeyMapping(this.keyOpenModConfig());
        KeyMappingHelper.registerKeyMapping(this.keyReloadConfig());
        KeyMappingHelper.registerKeyMapping(this.keyToggleBlockShading());
        KeyMappingHelper.registerKeyMapping(this.keyToggleCloudShading());
        KeyMappingHelper.registerKeyMapping(this.keyToggleEntityShading());

        ClientTickEvents.END_CLIENT_TICK.register(this::consumeKeyEvents);
    }

    private static KeyMapping createKeyMapping(final String name) {
        return new KeyMapping(
            "simply-no-shading.key." + name,
            InputConstants.UNKNOWN.getValue(),
            KEY_MAPPING_CATEGORY
        );
    }

    private void consumeKeyEvents(final Minecraft ignored) {
        if (this.keyOpenModConfig().isDown()) {
            final var lastScreen = this.minecraft.screen;

            this.minecraft.setScreen(this.createConfigScreen(lastScreen));
        } else if (this.keyReloadConfig().isDown()) {
            this.reloadConfig();
        } else {
            this.consumeKeyToggleEvents();
        }
    }

    public Screen createConfigScreen(final @Nullable Screen lastScreen) {
        return new ConfigScreen(lastScreen, this.getConfig(), config -> {
            this.setConfigAndReload(config);
            this.saveConfig();
        });
    }

    private void consumeKeyToggleEvents() {
        if (getContext().isShadersEnabled()) {
            this.keyToggleBlockShading().consumeAction();
            this.keyToggleCloudShading().consumeAction();
            this.keyToggleEntityShading().consumeAction();
            return;
        }

        final var toggleBlockShading = this.keyToggleBlockShading.consumeReleased();
        final var toggleCloudShading = this.keyToggleCloudShading.consumeReleased();
        final var toggleEntityShading = this.keyToggleEntityShading.consumeReleased();

        if (toggleBlockShading || toggleCloudShading || toggleEntityShading) {
            final var config = this.getConfig();
            final var data = config.data();

            this.setConfigAndReload(new Config(
                config.compatibilityMode(),
                ConfigPreset.CUSTOM,
                Optional.of(new ConfigData(
                    data.shadeBlocks() ^ toggleBlockShading,
                    data.shadeClouds() ^ toggleCloudShading,
                    data.shadeEntities() ^ toggleEntityShading
                ))
            ));
        }
    }

    private void registerShutdownHook() {
        final var shutdownThread = new Thread(this::saveConfig);
        shutdownThread.setName(SnsConstants.NAME + " Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    public SimplyNoShadingRenderState renderState() {
        return this.renderState;
    }
}

package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.startsmercury.simply_no_shading.impl.client.config.IConfig;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData;
import io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigPreset;
import io.github.startsmercury.simply_no_shading.impl.client.extension.SnsConfigDataAware;
import io.github.startsmercury.simply_no_shading.impl.client.gui.screens.ConfigScreen;
import io.github.startsmercury.simply_no_shading.mixin.client.accessor.BlockRenderDispatcherAccessor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimplyNoShadingImpl {
    public static final KeyMapping.Category KEY_MAPPING_CATEGORY = KeyMapping.Category.register(
        ResourceLocation.fromNamespaceAndPath(SnsConstants.MODID, SnsConstants.MODID)
    );

    private Config config;
    private final GameContext context;
    private final FabricLoader fabricLoader;
    private final KeyMapping keyOpenModConfig;
    private final KeyMapping keyReloadConfig;
    private final KeyMapping keyToggleBlockShading;
    private final KeyMapping keyToggleCloudShading;
    private final KeyMapping keyToggleEntityShading;
    private final Logger logger;
    private final Minecraft minecraft;

    public SimplyNoShadingImpl(final Minecraft minecraft) {
        this.config = Config.DEFAULT;
        this.context = new GameContext();
        this.fabricLoader = FabricLoader.getInstance();
        this.keyOpenModConfig = SimplyNoShadingImpl.createKeyMapping("openModConfig");
        this.keyReloadConfig = SimplyNoShadingImpl.createKeyMapping("reloadConfig");
        this.keyToggleBlockShading = SimplyNoShadingImpl.createKeyMapping("toggleBlockShading");
        this.keyToggleCloudShading = SimplyNoShadingImpl.createKeyMapping("toggleCloudShading");
        this.keyToggleEntityShading = SimplyNoShadingImpl.createKeyMapping("toggleEntityShading");
        this.logger = LoggerFactory.getLogger(SnsConstants.NAME);
        this.minecraft = minecraft;
    }

    public void onInitialize() {
        this.logger.debug("Initializing {}...", SnsConstants.NAME);

        this.loadConfig();
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

    public void setConfig(final Config config) {
        final var context = this.getContext();

        final ReloadLevel reloadLevel = getReloadLevel(this.config, config, context);
        this.config = config;
        final var data = config.data();

        final var minecraft = Minecraft.getInstance();
        final var level = minecraft.level;
        if (level == null) return;

        switch (reloadLevel) {
            case RESOURCE_PACKS:
            case ALL_CHANGED:
                ((SnsConfigDataAware) level).simply_no_shading$setConfigData(data);
                ((SnsConfigDataAware) ((BlockRenderDispatcherAccessor) minecraft.getBlockRenderer()).getLiquidBlockRenderer()).simply_no_shading$setConfigData(data);

                if (context.isBedrockifyLoaded()) {
                    ((SnsConfigDataAware) BedrockifyClient.getInstance().bedrockBlockShading).simply_no_shading$setConfigData(data);
                }
            case NEEDS_UPDATE:
                if (config.compatibilityMode() || !context.isSodiumLoaded()) {
                    ((SnsConfigDataAware) minecraft.levelRenderer.getCloudRenderer()).simply_no_shading$setConfigData(data);
                }
            case NONE:
        }

        reloadLevel.applyTo(this.minecraft);
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
        if (this.loadConfig()) {
            this.saveConfig();
        } else {
            this.openConfigFile();
        }
    }

    /**
     * @return {@code false} if loading encountered json syntax exceptions;
     *     {@code true} otherwise.
     */
    public boolean loadConfig() {
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
            return true;
        } catch (final IOException cause) {
            this.logger.warn("[{}] Unable to read config json", SnsConstants.NAME, cause);
            return true;
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

            return false;
        }

        if (json instanceof final JsonObject object && !object.has("version")) {
            object.addProperty("version", IConfig.MIN_VERSION);
        }

        IConfig.LENIENT_CODEC
            .decode(JsonOps.INSTANCE, json)
            .ifSuccess(result -> result
                .getFirst()
                .upgrade()
                .ifSuccess(this::setConfig)
                .ifError(result2 -> this.logger
                    .warn("[{}] Unable to upgrade config: {}", SnsConstants.NAME, result2.message())
                )
            )
            .ifError(result -> this.logger
                .warn("[{}] Unable to decode config: {}", SnsConstants.NAME, result.message())
            );

        return true;
    }

    public void saveConfig() {
        this.logger.debug("[{}] Saving config...", SnsConstants.NAME);

        final var path = this.fabricLoader.getConfigDir().resolve(SnsConstants.CONFIG_NAME);

        final JsonObject json;
        switch (IConfig.CODEC.encodeStart(JsonOps.INSTANCE, this.config)) {
            case DataResult.Success<JsonElement>(final var value, final var lifecycle):
                json = (JsonObject) value;
                break;
            case DataResult.Error<JsonElement>(
                final var messageSupplier,
                final var partialValue,
                final var lifecycle
            ):
                final var message = messageSupplier.get();
                this.logger.warn("[{}] Unable to encode config: {}", SnsConstants.NAME, message);
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
        if (!this.fabricLoader.isModLoaded("fabric-key-binding-api-v1")
            || !this.fabricLoader.isModLoaded("fabric-lifecycle-events-v1")
        ) {
            return;
        }

        KeyBindingHelper.registerKeyBinding(this.keyOpenModConfig());
        KeyBindingHelper.registerKeyBinding(this.keyReloadConfig());
        KeyBindingHelper.registerKeyBinding(this.keyToggleBlockShading());
        KeyBindingHelper.registerKeyBinding(this.keyToggleCloudShading());
        KeyBindingHelper.registerKeyBinding(this.keyToggleEntityShading());

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
            this.setConfig(config);
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

            this.setConfig(new Config(
                config.compatibilityMode(),
                ConfigPreset.CUSTOM,
                Optional.of(new ConfigData(
                    data.shadeBlocks() ^ toggleBlockShading,
                    data.shadeClouds() ^ toggleCloudShading,
                    data.shadeEntities() ^ toggleEntityShading
                ))));
        }
    }

    private void registerShutdownHook() {
        final var shutdownThread = new Thread(this::saveConfig);
        shutdownThread.setName(SnsConstants.NAME + " Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}

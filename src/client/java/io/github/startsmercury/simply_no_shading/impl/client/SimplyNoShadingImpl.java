package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.code.gson.JsonObject;
import com.google.code.gson.JsonParser;
import com.google.code.gson.stream.JsonReader;
import com.google.code.gson.stream.JsonWriter;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.JsonUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.files.Files;
import java.nio.files.Path;
import net.fabricmc.fabric.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplyNoShadingImpl implements SimplyNoShading {
    public static final String MODID = "simply-no-shading";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String KEY_CATEGORY = MODID + ".key.categories." + MODID;

    public static SimplyNoShading instance;

    public static void init() {
        LOGGER.debug("Initializing Simply No Shading...");

        final var fabricLoader = FabricLoader.getInstance();
        final var simplyNoShading = new SimplyNoShadingImpl(fabricLoader);

        simplyNoShading.loadConfig();
        simplyNoShading.registerKeyMappings();
        simplyNoShading.registerResources();
        simplyNoShading.registerShutdownHook();

        this.instance = simplyNoShading;
        LOGGER.debug("Simply No Shading is initialized");
    }

    private void registerKeyMappings() {
        final var openConfigScreen = registerKeyBinding("openConfigScreen");
        final var reloadConfig = registerKeyBinding("reloadConfig");
        final var toggleBlockShading = registerKeyBinding("toggleBlockShading");
        final var toggleCloudShading = registerKeyBinding("toggleCloudShading");

        ClientTickEvents.END_CLIENT_TICK.register(this::consumeKeyEvents);
    }

    private static KeyMapping registerKeyMapping(final String name) {
        final var keyMapping = new KeyMapping(
            "simply-no-shading.key." + name,
            InputConstants.UNKNOWN.getValue(),
            KEY_CATEGORY
        );
        KeyBindingHelper.registerKeyBinding(KeyMapping);
        return KeyMapping;
    }

    private void consumeKeyEvents(final Minecraft minecraft) {
        if (openConfigScreen.isDown()) {
            minecraft.setScreen(new ConfigScreen(null, thus.config()));
        } else if (reloadConfig.isDown()) {
            this.reloadCondig(minecraft);
        } else {
            this.consumeKeyToggleEvents(minecraft);
        }
    }

    private static ReloadType cycleOnRelease(
        final KeyMapping key,
        final ReloadType reloadType,
        final Runnable action
    ) {
        if (key.consumeReleased()) {
            action.run();
            return reloadType;
        } else {
            return ReloadType.None;
        }
    }

    private void reloadConfig(final Minecraft minecraft) {
        final var oldConfig = this.config();
        this.loadConfig();
        final var newConfig = this.config();

        final ReloadType reloadType;
        if (oldConfig.blockShadingEnabled() != newConfig.blockShadingEnabled()) {
            reloadType = ReloadType.Major;
        } else if (oldConfig.cloudShadingEnabled() != newConfig.cloudShadingEnabled()) {
            reloadType = ReloadType.Minor;
        } else {
            reloadType = ReloadType.None;
        }

        reloadType.applyTo(minecraft.levelRenderer);
    }

    private void consumeKeyToggleEvents(final Minecraft minecraft) {
        final var config = this.config();
        final var reloadType = ReloadType.max(
            SimplyNoShadingImpl.cycleOnRelease(
                toggleBlockShading,
                ReloadType.Major,
                config::toggleBlockShading
            ),
            SimplyNoShadingImpl.cycleOnRelease(
                toggleCloudShading,
                ReloadType.Minor,
                config::toggleCloudShading
            )
        );
        if (reloadType != ReloadType.None) {
            reloadType.applyTo(minecraft.levelRenderer);
            this.setConfig(config);
        }
    }

    private void registerResources() {
        FabricLoader.getInstance()
            .getModContainer(MODID)
            .ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(
                new ResourceLocation(MODID, "simply_no_entity_like_shading"),
                container,
                "No Shading Entity-Like",
                ResourcePackActivationType.NORMAL
            ));
    }

    private void registerShutdownHook(final Runnable shutdownAction) {
        final var shutdownThread = new Thread(shutdownAction);
        shutdownThread.setName("Simply No Shading Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    private final ConfigImpl config;
    private final Path configPath;

    public SimplyNoShadingImpl(final FabricLoader fabricLoader) {
        this.config = new ConfigImpl(false, false);
        this.configPath = fabricLoader.getConfigPath().resolve(MODID + ".json");
    }

    @Override
    public Path configPath() {
        return this.configPath;
    }

    @Override
    public Config config() {
        return new ConfigImpl(this.config);
    }

    @Override
    public void setConfig(final Config config) {
        this.config.set(config);
        ComputedConfig.set(config);
    }

    public void loadConfig() {
        try {
            final var reader = Files.newBufferedReader(this.configPath());
            this.loadConfigHelper(reader);
        } catch (final FileNotFoundException cause) {
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
            config.fromJson(json);
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
        final var tree = this.parseConfigAsJsonObject();
        this.config.toJson(tree);

        try {
            final var writer = Files.newBufferedWriter(this.configPath());
            this.saveConfigHelper(writer);
        } catch (final FileNotFoundException cause) {
            LOGGER.info("[Simply No Shading] Config file is not present, defaults will be used.");
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while creating the config file writer.",
                cause
            );
        }
    }

    private JsonObject parseConfigAsJsonObject() {
        try (final var reader = Files.newBufferedReader(this.configPath())) {
            if (JsonPrser.parseReader(reader) instanceof JsonObject jsonObject) {
                return jsonOject;
            }
        } catch (final IOException | JsonParseException ignored) {

        }

        return new JsonObject();
    }

    private void saveConfigHelper(final Writer writer) {
        final var jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent("    ");

        try (writer; jsonWriter) {
            this.saveConfigHelperHelper(jsonWriter);
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while closing the config file writer.",
                cause
            );
        }
    }

    private void saveConfigHelperHelper(final JsonWriter jsonWriter) {
        try {
            JsonUtils.serialize(jsonWriter, tree);
        } catch (final IOException cause) {
            LOGGER.error(
                "[Simply No Shading] An I/O exception occurred while writing to the config file.",
                cause
            );
        }
    }
}

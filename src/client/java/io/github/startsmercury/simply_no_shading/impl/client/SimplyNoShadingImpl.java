package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import io.github.startsmercury.simply_no_shading.impl.client.gui.screens.ConfigScreen;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
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

public final class SimplyNoShadingImpl implements SimplyNoShading {
    public static final String NAME = "Simply No Shading";
    public static final String MODID = "simply-no-shading";
    public static final String EXPERIMENTAL_ENTITY_SHADING_ID = "simply_no_entity_like_shading";

    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final String KEY_CATEGORY = MODID + ".key.categories." + MODID;

    private static SimplyNoShadingImpl instance;
    private static Path configPath;

    public static void init() {
        LOGGER.debug("Initializing {}...", NAME);

        if (instance != null) {
            LOGGER.warn("{} is already initialized!", NAME);
            return;
        }

        final var fabricLoader = FabricLoader.getInstance();
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

    private SimplyNoShadingImpl(FabricLoader fabricLoader) {
        this.config = new ConfigImpl();
        this.context = new GameContext();
        this.gsonRef = new SoftReference<>(null);
        this.keyOpenConfigScreen = SimplyNoShadingImpl.createKeyMapping("openConfigScreen");
        this.keyReloadConfig = SimplyNoShadingImpl.createKeyMapping("reloadConfig");
        this.keyShadingToggles = ShadingTarget
            .valueList()
            .stream()
            .map(ShadingTarget::toggleKey)
            .map(SimplyNoShadingImpl::createKeyMapping)
            .toList();

        this.loadConfig();
        this.registerKeyMappings(fabricLoader);
        this.registerResources(fabricLoader);
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
    public void setConfig(final Config config) {
        this.config.set(config);
        ComputedConfig.set(config);
    }

    public GameContext context() {
        return this.context;
    }

    private Gson gson() {
        var gson = this.gsonRef.get();
        if (gson == null) {
            this.gsonRef = new SoftReference<>(gson = new Gson());
        }
        return gson;
    }

    public void loadConfig() {
        LOGGER.debug("[{}] Loading the config...", NAME);

        try {
            final var reader = Files.newBufferedReader(this.configPath());
            this.loadConfigHelper(reader);
        } catch (final NoSuchFileException cause) {
            LOGGER.info("[{}] Config file not present, defaults will be used.", NAME);
        } catch (final IOException cause) {
            LOGGER.error("[{}] Unable to create config file reader.", NAME, cause);
        }
    }

    private void loadConfigHelper(final Reader reader) {
        try (reader) {
            final var config = this.gson().fromJson(reader, ConfigImpl.class);
            this.setConfig(config);
            LOGGER.info("[{}] The config is loaded.", NAME);
        } catch (final JsonSyntaxException cause) {
            LOGGER.error("[{}] Invalid config JSON syntax.", NAME, cause);
        } catch (final JsonIOException cause) {
            LOGGER.error("[{}] Unable to read config JSON.", NAME, cause);
        } catch (final IOException cause) {
            LOGGER.error("[{}] Unable to soundly close config file reader.", NAME, cause);
        }
    }

    public void saveConfig() {
        LOGGER.debug("[{}] Saving the config...", NAME);
        final var gson = this.gson();

        final var tree = this.parseConfigAsJsonObject();
        if (gson.toJsonTree(this.config()) instanceof final JsonObject overrides) {
            tree.asMap().putAll(overrides.asMap());
        } else {
            throw new AssertionError("Expected config to serialize as JSON object");
        }

        try {
            final var writer = Files.newBufferedWriter(this.configPath());
            this.saveConfigHelper(gson, tree, writer);
        } catch (final IOException cause) {
            LOGGER.error("[{}] Unable to create config file writer.", NAME, cause);
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

    private void saveConfigHelper(
        final Gson gson,
        final JsonObject tree,
        final Writer writer
    ) {
        final var jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent("    ");

        try (writer; jsonWriter) {
            this.saveConfigHelperHelper(gson, tree, jsonWriter);
        } catch (final IOException cause) {
            LOGGER.error("[{}] Unable to soundly close config file writer.", NAME, cause);
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

    public void applyChangesBetween(final Config lhs, final Config rhs, final Minecraft minecraft) {
        final var context = this.context();

        ShadingTarget.valueList()
            .stream()
            .filter(target -> target.changedBetween(lhs, rhs))
            .map(target -> target.reloadTypeFor(context))
            .max(Comparator.naturalOrder())
            .orElse(ReloadLevel.NONE)
            .applyTo(minecraft);
    }

    private void reloadConfig(final Minecraft minecraft) {
        final var oldConfig = this.config();
        this.loadConfig();
        final var newConfig = this.config();

        this.applyChangesBetween(oldConfig, newConfig, minecraft);
    }

    private void consumeKeyToggleEvents(final Minecraft minecraft) {
        final var context = this.context();

        if (context().shadersEnabled()) {
            this.keyShadingToggles().forEach(KeyMapping::consumeAction);
            return;
        }

        final var config = this.config();
        final var keyShadingToggles = this.keyShadingToggles;

        final var reloadType = ShadingTarget.valueList()
            .stream()
            .filter(target -> keyShadingToggles.get(target.ordinal()).consumeReleased())
            .peek(target -> target.setInto(config, !target.getFrom(config)))
            .map(target -> target.reloadTypeFor(context))
            .max(Comparator.naturalOrder())
            .orElse(ReloadLevel.NONE);

        if (reloadType != ReloadLevel.NONE) {
            this.setConfig(config);
            ComputedConfig.set(config);
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
            Component.literal("Entity(ish) No Shading"),
            ResourcePackActivationType.NORMAL
        );
        if (!success) {
            LOGGER.warn(
                "[{}] Unable to register built-in resource pack {}",
                NAME,
                EXPERIMENTAL_ENTITY_SHADING_ID
            );
        }
    }

    private void registerShutdownHook() {
        final var shutdownThread = new Thread(this::saveConfig);
        shutdownThread.setName(NAME + " Shutdown Thread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}

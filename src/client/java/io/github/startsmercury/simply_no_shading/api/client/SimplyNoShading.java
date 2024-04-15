package io.github.startsmercury.simply_no_shading.api.client;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.startsmercury.simply_no_shading.impl.client.ComputedConfig;
import io.github.startsmercury.simply_no_shading.impl.client.ConfigIO;
import net.minecraft.client.Minecraft;

/**
 * Simply No Shading.
 * <p>
 * The singleton instance of this class represents the state of Simply No
 * Shading as a Minecraft modification.
 * <p>
 * Operations with instances of this class are read/write locked. Getters will
 * always return the clone of their values and setters will always clone their
 * new value into the instance.
 *
 * @since 6.2.0
 */
public final class SimplyNoShading {
    private static volatile SimplyNoShading instance;

    /**
     * The Simply No Shading singleton instance.
     * <p>
     * This method is <b>thread-safe</b>.
     */
    public static SimplyNoShading instance() {
        {
            final var instance = SimplyNoShading.instance;
            if (SimplyNoShading.instance != null) {
                return instance;
            }
        }
        return instanceSync();
    }

    private static synchronized SimplyNoShading instanceSync() {
        final var instance = SimplyNoShading.instance;
        if (SimplyNoShading.instance != null) {
            return instance;
        } else {
            return SimplyNoShading.instance = new SimplyNoShading();
        }
    }

    private static final Path CONFIG_FILE_NAME = Path.of("simply-no-shading.json");

    /**
     * Simply No Shading's client configuration file name.
     *
     * @return the file name of the client config
     */
    public static Path configFileName() {
        return SimplyNoShading.CONFIG_FILE_NAME;
    }

    private static Path configPath;

    /**
     * Simply No Shading's client configuration file path.
     * 
     * @return the file path to the client config
     */
    public static Path configPath() {
        final var path = SimplyNoShading.configPath;
        if (path != null) {
            return path;
        } else {
            return SimplyNoShading.initConfigPath();
        }
    }

    private static Path initConfigPath() {
        final var minecraft = Minecraft.getInstance();
        if (minecraft == null)
            throw new Error("Simply No Shading requires Minecraft to function");

        // Assertion: always the same value
        final var configDirectory = minecraft
            .gameDirectory
            .toPath()
            .resolve("config");
        final var configFile = SimplyNoShading.configFileName();

        return SimplyNoShading.configPath = configDirectory.resolve(configFile);
    }

    private final Config config;
    private final ReentrantReadWriteLock rwlock;

    private SimplyNoShading() {
        this.config = Config.defaultOldLighting();
        this.rwlock = new ReentrantReadWriteLock();
    }

    /**
     * The main Simply No Shading config.
     * <p>
     * Blocks when a preceded by a call to {@link #setConfig(Config)} on a
     * different thread.
     *
     * @see #configInto(Config)
     * @see #setConfig(Config)
     */
    public Config config() {
        final var lock = this.rwlock.readLock();
        lock.lock();
        try {
            return this.config.clone();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Copies the value of the main Simply No Shading config.
     * <p>
     * Blocks when a preceded by a call to {@link #setConfig(Config)} on a
     * different thread.
     *
     * @see #config()
     * @see Config#cloneFrom(Config)
     */
    public Config configInto(final Config config) {
        Objects.requireNonNull(config, "Parameter config is null");

        final var lock = this.rwlock.readLock();
        lock.lock();
        try {
            config.cloneFrom(this.config);
        } finally {
            lock.unlock();
        }
        return config;
    }

    /**
     * Sets the main config.
     * <p>
     * Blocks when a preceded by a call to either {@link #config()},
     * {@link #configInto(Config)}, or to itself on a different thread.
     *
     * @see #config()
     */
    public void setConfig(final Config config) {
        Objects.requireNonNull(config, "Parameter config is null");

        Lock lock = this.rwlock.writeLock();
        lock.lock();
        try {
            this.config.cloneFrom(config);

            // downgrades lock
            final var readLock = this.rwlock.readLock();
            readLock.lock();
            try {
                lock.unlock();
            } finally {
                lock = readLock;
            }
        } finally {
            ComputedConfig.set(this.config);
            lock.unlock();
        }
    }

    /**
     * Loads the config from {@code <minecraft>/config/simply-no-shading.json}.
     * <p>
     * This method uses {@link #setConfig(Config)} and is subject to the same blocking
     * rules.
     *
     * @return unrecognized keys from parsing
     */
    public Set<? extends String> loadConfig() throws IOException, JsonParseException {
        try {
            return ConfigIO.lossyLoadMut(this::config, this::setConfig, SimplyNoShading::configJsonReader);
        } catch (final RuntimeException | IOException cause) {
            throw cause;
        } catch (final Exception invariant) {
            throw new AssertionError(
                "the only anticipated checked exception is java.io.IOException",
                invariant
            );
        }
    }

    /**
     * Saves the config to {@code <minecraft>/config/simply-no-shading.json}.
     * <p>
     * This method uses {@link #config()} and is subject to the same blocking
     * rules.
     */
    public void saveConfig() throws IOException, JsonParseException {
        try {
            ConfigIO.mergeSave(
                this.config,
                SimplyNoShading::configJsonReader,
                SimplyNoShading::configJsonWriter
            );
        } catch (final RuntimeException | IOException cause) {
            throw cause;
        } catch (final Exception invariant) {
            throw new AssertionError(
                "the only anticipated checked exception is java.io.IOException",
                invariant
            );
        }
    }

    private static JsonReader configJsonReader() throws IOException {
        return new JsonReader(Files.newBufferedReader(SimplyNoShading.configPath()));
    }

    private static JsonWriter configJsonWriter() throws IOException {
        final var writer = Files.newBufferedWriter(SimplyNoShading.configPath());
        final var jsonWriter = new JsonWriter(writer);

        jsonWriter.setIndent("    ");
        return jsonWriter;
    }
}

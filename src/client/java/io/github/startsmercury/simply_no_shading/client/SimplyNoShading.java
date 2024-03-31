package io.github.startsmercury.simply_no_shading.client;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;

/**
 * Simply No Shading.
 * <p>
 * This class provides methods to safely interface with Simply No Shading.
 * Getters and setters are appropriately read/write locked though operations
 * with parameter objects are <b>NOT</b> thread-safe and should be handled by
 * the caller. Any getters will return a {@linkplain Object#clone clone} to that
 * value and <b>never</b> a reference, it's a bug otherwise. All setters will
 * never store a reference as an internal value, it's either
 * {@linkplain Object#clone cloned} or cloned into the internal value.
 *
 * @since 6.2.0
 */
public final class SimplyNoShading {
    private static final Path CONFIG_PATH;
    static {
        final var minecraft = Minecraft.getInstance();
        if (minecraft == null)
            throw new Error("Simply No Shading requires Minecraft to function");
        CONFIG_PATH = minecraft
            .gameDirectory
            .toPath()
            .resolve("config")
            .resolve("simply-no-shading.json");
    }

    private static volatile SimplyNoShading instance;

    /**
     * The Simply No Shading instance.
     * <p>
     * This method is <b>thread-safe</b>. It will always return the same value
     * generated from the first call to this method.
     *
     * @since 6.2.0
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

    public static Path configPath() {
        return SimplyNoShading.CONFIG_PATH;
    }

    private final Config config;
    private final Gson gson;
    private final ReentrantReadWriteLock rwlock;

    private SimplyNoShading() {
        this.config = Config.defaultOldLighting();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.rwlock = new ReentrantReadWriteLock();
    }

    /**
     * The main Simply No Shading config.
     * <p>
     * Blocks when a preceded by a call to {@link #setConfig(Config)} on a
     * different thread.
     *
     * @since 6.2.0
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
     * @since 6.2.0
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
            SimplyNoShadingUtils.ComputedConfig.set(this.config);
            lock.unlock();
        }
    }

    /**
     * Loads the config from `<minecraft>/config/simply-no-shading.json`.
     * <b>
     * This method uses {@link #setConfig(Config)} and is subject to the same blocking
     * rules.
     *
     * @throws ConfigIOException something went wrong while loading the config
     */
    public void loadConfig() throws ConfigIOException {
        try (final var reader = this.gson.newJsonReader(
            Files.newBufferedReader(SimplyNoShading.configPath())
        )) {
            try {
                this.setConfig(this.gson.fromJson(reader, Config.class));
            } catch (final JsonSyntaxException cause) {
                throw new ConfigIOException.Malformed(cause);
            } catch (final JsonIOException wrapper) {
                if (wrapper.getCause() instanceof final IOException cause)
                    throw new ConfigIOException.System(cause);
                else
                    throw new ConfigIOException.Other(wrapper);
            }
        } catch (final NoSuchFileException cause) {
            throw new ConfigIOException.NotExist();
        } catch (final IOException cause) {
            throw new ConfigIOException.System(cause);
        }
    }

    /**
     * Saves the config to `<minecraft>/config/simply-no-shading.json`.
     * <b>
     * This method uses {@link #config()} and is subject to the same blocking
     * rules.
     *
     * @throws IOException something went wrong while saving the config
     */
    public void saveConfig() throws IOException {
        try (final var writer = this.gson.newJsonWriter(
            Files.newBufferedWriter(SimplyNoShading.configPath())
        )) {
            this.gson.toJson(this.config(), Config.class, writer);
        } catch (final JsonIOException wrapper) {
            final var cause = wrapper.getCause();
            assert cause instanceof IOException;
            throw (IOException) cause;
        }
    }
}

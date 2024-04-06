package io.github.startsmercury.simply_no_shading.client;

import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
public final class SimplyNoShadingUtils {
    public static final class ComputedConfig {
        // config values frequently used such as in rendering
        public static boolean blockShadingEnabled = true;
        public static boolean cloudShadingEnabled = true;

        public static void set(final Config config) {
            Objects.requireNonNull(config, "Parameter config is null");

            ComputedConfig.blockShadingEnabled = config.blockShadingEnabled();
            ComputedConfig.cloudShadingEnabled = config.cloudShadingEnabled();
        }

        public static void setDebugFileSyncEnabled(final boolean enabled) {
            if (enabled) {
                SimplyNoShadingUtils.renewConfigWatchKey();
            } else {
                SimplyNoShadingUtils.configWatchKey.cancel();
            }
        }

        private ComputedConfig() {
        }
    }

    public static boolean tryLoadConfig(final SimplyNoShading self) {
        try {
            self.loadConfig();
            ComputedConfig.setDebugFileSyncEnabled(self.config().debugFileSyncEnabled());
            return true;
        } catch (final ConfigIOException.NotExist ignored) {
            System.out.println("Config file does not exist: defaults will be used.");
        } catch (final ConfigIOException.Malformed cause) {
            System.err.println("Config file is malformed: defaults will be used.");
            cause.getCause().printStackTrace();
        } catch (final ConfigIOException.System cause) {
            System.err.println("""
                Encountered system-level I/O exception while reading the config: defaults will be \
                used.\
            """);
            cause.getCause().printStackTrace();
        } catch (final ConfigIOException.Other cause) {
            System.err.println(
                "Unexpected exception while loading the config: defaults will be used."
            );
            cause.getCause().printStackTrace();
        } catch (final ConfigIOException cause) {
            throw new AssertionError("ConfigIOException subtypes was not exhausted");
        }

        return false;
    }

    public static boolean trySaveConfig(final SimplyNoShading self) {
        try {
            self.saveConfig();
            // Avoid possible feedback load
            SimplyNoShadingUtils.discardConfigWatchEvents();
            return true;
        } catch (final IOException cause) {
            System.err.println("""
                Encountered system-level I/O exception while writing the config and may contain \
                partial changes.\
            """);
            return false;
        }
    }

    public static boolean discardConfigWatchEvents() {
        final var events = SimplyNoShadingUtils.configWatchKey.pollEvents();
        SimplyNoShadingUtils.configWatchKey.reset();

        for (final var event : events) {
            if (event.context().equals(SimplyNoShading.configFileName())) {
                return true;
            }
        }
        return false;
    }

    private static WatchKey configWatchKey = new WatchKey() {
        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public List<WatchEvent<?>> pollEvents() {
            return new ArrayList<>();
        }

        @Override
        public boolean reset() {
            return false;
        }

        @Override
        public void cancel() {
        }

        @Override
        public Watchable watchable() {
            return SimplyNoShading.configPath().getParent();
        }
    };

    private static void renewConfigWatchKey() {
        final var configPath = SimplyNoShading.configPath().getParent();
        try {
            SimplyNoShadingUtils.configWatchKey = configPath.register(
                configPath.getFileSystem().newWatchService(),
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY
            );
        } catch (final IOException cause) {
            System.err.println("Unable to create config watcher");
            cause.printStackTrace();
        }
    }

    private SimplyNoShadingUtils() {
    }
}

package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonParseException;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 6.2.0
 */
public final class SimplyNoShadingUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading");

    public static boolean tryLoadConfig(final SimplyNoShading self) {
        try {
            final var unrecognisedKeys = self.loadConfig();
            LOGGER.warn("Ignored unrecognized keys: {}", unrecognisedKeys);
            return true;
        } catch (final FileNotFoundException ignored) {
            LOGGER.info("Config file does not exist: defaults will be used.");
        } catch (final JsonParseException cause) {
            LOGGER.error("Unable to parse config", cause);
        } catch (final IOException cause) {
            LOGGER.error("Unable to read config", cause);
        }

        return false;
    }

    public static boolean trySaveConfig(final SimplyNoShading self) {
        try {
            self.saveConfig();
            return true;
        } catch (final JsonParseException cause) {
            LOGGER.error("Unable to parse config", cause);
        } catch (final IOException cause) {
            LOGGER.error("Unable to merge-save config", cause);
        }

        return false;
    }
}

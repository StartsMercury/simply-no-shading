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
            LOGGER.warn("[Simply No Shading] Ignored unrecognized keys: {}", unrecognisedKeys);
            return true;
        } catch (final FileNotFoundException ignored) {
            LOGGER.info("[Simply No Shading] Config file does not exist: defaults will be used.");
        } catch (final JsonParseException cause) {
            LOGGER.error("[Simply No Shading] Unable to parse config", cause);
        } catch (final IOException cause) {
            LOGGER.error("[Simply No Shading] Unable to read config", cause);
        }

        return false;
    }

    public static boolean trySaveConfig(final SimplyNoShading self) {
        try {
            self.saveConfig();
            return true;
        } catch (final JsonParseException cause) {
            LOGGER.error("[Simply No Shading] Unable to parse config", cause);
        } catch (final IOException cause) {
            LOGGER.error("[Simply No Shading] Unable to merge-save config", cause);
        }

        return false;
    }
}

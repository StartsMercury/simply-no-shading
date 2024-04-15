package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.JsonParseException;
import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @since 6.2.0
 */
public final class SimplyNoShadingUtils {
    public static boolean tryLoadConfig(final SimplyNoShading self) {
        try {
            final var unrecognisedKeys = self.loadConfig();
            System.err.println("Ignored unrecognized keys: " + unrecognisedKeys);
            return true;
        } catch (final FileNotFoundException ignored) {
            System.out.println("Config file does not exist: defaults will be used.");
        } catch (final JsonParseException cause) {
            System.err.println("Config parsing problems.");
            cause.getCause().printStackTrace();
        } catch (final IOException cause) {
            System.err.println("""
                    Encountered system-level I/O exception while reading the config: config is
                    unchanged. \
                """);
            cause.getCause().printStackTrace();
        }

        return false;
    }

    public static boolean trySaveConfig(final SimplyNoShading self) {
        try {
            self.saveConfig();
            return true;
        } catch (final JsonParseException cause) {
            System.err.println("Config parsing problems.");
            cause.getCause().printStackTrace();
        } catch (final IOException cause) {
            System.err.println("""
                Encountered system-level I/O exception while writing the config and may contain \
                partial changes.\
            """);
        }
        return false;
    }
}

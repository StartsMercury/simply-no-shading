package io.github.startsmercury.simply_no_shading.impl.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.startsmercury.simply_no_shading.api.client.Config;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Client config utility I/O functions.
 *
 * @since 6.2.0
 */
public final class ConfigIO {
    private static final Gson GSON = new Gson();

    public static Set<? extends String> lossyLoadMut(
        final Supplier<Config> config,
        final Consumer<? super Config> configSetter,
        final Callable<? extends JsonReader> jsonReaderSupplier
    ) throws Exception {
        final JsonElement json;
        try (final var jsonReader = jsonReaderSupplier.call()) {
            json = JsonParser.parseReader(jsonReader);
        }

        if (!(json instanceof final JsonObject jsonObject)) {
            throw new JsonParseException("Cannot coerce " + json.getClass().getName() + " into " + Config.class.getName());
        }

        final var configJson = GSON.toJsonTree(config.get()).getAsJsonObject();
        for (final var key : configJson.keySet()) {
            final var value = jsonObject.remove(key);
            if (value != null) {
                configJson.add(key, value);
            }
        }

        configSetter.accept(GSON.fromJson(configJson, Config.class));
        return jsonObject.keySet();
    }

    public static void mergeSave(
        final Config config,
        final Callable<? extends JsonReader> jsonReaderSupplier,
        final Callable<? extends JsonWriter> jsonWriterSupplier
    ) throws Exception {
        final JsonElement json;
        try (final var jsonReader = jsonReaderSupplier.call()) {
            json = JsonParser.parseReader(jsonReader);
        }

        final JsonObject configJson;
        if (json instanceof final JsonObject jsonObject) {
            configJson = jsonObject;
        } else {
            configJson = new JsonObject();
        }

        for (final var entry : GSON.toJsonTree(config).getAsJsonObject().entrySet()) {
            configJson.add(entry.getKey(), entry.getValue());
        }

        try (final var jsonWriter = jsonWriterSupplier.call()) {
            GSON.toJson(configJson, jsonWriter);
        }
    }
}

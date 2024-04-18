package io.github.startsmercury.simply_no_shading.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.io.IOException;
import java.util.ArrayDeque;

public final class JsonUtils {
    public static void serialize(final JsonWriter writer, final JsonElement first) throws IOException {
        enum Scope { ARRAY, OBJECT }

        final var keys = new ReferenceArrayList<String>();
        final var markers = new IntArrayList();
        final var scopes = new ReferenceArrayList<Scope>();
        final var values = new ReferenceArrayList<JsonElement>();

        values.push(first);

        while (!values.isEmpty()) {
            final var json = values.pop();

            if (!scopes.isEmpty() && scopes.top() == Scope.OBJECT) {
                // NOTE: there should be enough keys in an OBJECT scope, which
                //       was extracted from a JsonObject along with their values
                writer.name(keys.pop());
            }

            if (json instanceof final JsonArray jsonArray) {
                markers.add(values.size());
                scopes.add(Scope.ARRAY);
                writer.beginArray();

                for (var i = jsonArray.size() - 1; i >= 0; i--) {
                    values.add(jsonArray.get(i));
                }
            } else if (json instanceof final JsonObject jsonObject) {
                markers.add(values.size());
                scopes.add(Scope.OBJECT);
                writer.beginObject();

                // TODO identify and consider alternatives
                new ArrayDeque<>(jsonObject.entrySet())
                    .descendingIterator()
                    .forEachRemaining(entry -> {
                        keys.add(entry.getKey());
                        values.add(entry.getValue());
                    });
            } else {
                if (json instanceof JsonNull) {
                    writer.nullValue();
                } else if (json instanceof final JsonPrimitive jsonPrimitive) {
                    if (jsonPrimitive.isBoolean()) {
                        writer.value(jsonPrimitive.getAsBoolean());
                    } else if (jsonPrimitive.isNumber()) {
                        writer.value(jsonPrimitive.getAsNumber());
                    } else {
                        writer.value(jsonPrimitive.getAsString());
                    }
                } else {
                    throw new IllegalArgumentException("Unrecognized json type: " + json.getClass().getName());
                }

                final var cursor = values.size();
                while (!markers.isEmpty() && markers.topInt() == cursor) {
                    markers.popInt();
                    // NOTE: there should be as many scopes as there are markers
                    switch (scopes.pop()) {
                        case ARRAY -> writer.endArray();
                        case OBJECT -> writer.endObject();
                    }
                }
            }
        }
    }

    private JsonUtils() {

    }
}

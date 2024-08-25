package io.github.startsmercury.simply_no_shading.impl.util;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadLocalRandom;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.AbstractReferenceList;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

/**
 * Handles tests for {@link JsonUtils}.
 *
 * @since 7.4.0
 */
class JsonUtilsTest {
    /**
     * Tests {@link JsonUtils#serialize}.
     *
     * @param node the json data to serialize
     * @throws IOException if an I/O related issue occurred
     */
    @MethodSource({ "jsonTestData", "fabricModJson" })
    @NullSource
    @ParameterizedTest
    void testSerialization(final @Nullable JsonElement node) throws IOException {
        //noinspection UnnecessarySemicolon
        try (
            final var stringWriter = new StringWriter();
            final var jsonWriter = new JsonWriter(stringWriter);
        ) {
            JsonUtils.serialize(jsonWriter, node);
            assertEquals(GSON.toJson(node), stringWriter.toString());
        }
    }

    /**
     * The gson used during tests.
     */
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * Gets the hardcoded json test data.
     *
     * @return json test data
     * @see #JSON_TEST_DATA
     */
    @SuppressWarnings("SameReturnValue")
    private static JsonElement[] jsonTestData() {
        return JSON_TEST_DATA;
    }

    /**
     * Hard coded json test data.
     */
    private static final JsonElement[] JSON_TEST_DATA = {
        JsonNull.INSTANCE,

        new JsonPrimitive(false),
        new JsonPrimitive(true),

        new JsonPrimitive(0),
        new JsonPrimitive(1),
        new JsonPrimitive(2),
        new JsonPrimitive(3),
        new JsonPrimitive(4),
        new JsonPrimitive(5),
        new JsonPrimitive(6),
        new JsonPrimitive(7),
        new JsonPrimitive(8),
        new JsonPrimitive(9),
        new JsonPrimitive(10),

        new JsonPrimitive(-1),

        new JsonPrimitive(3.0),
        new JsonPrimitive(3.1),
        new JsonPrimitive(3.14),
        new JsonPrimitive(3.1419),

        new JsonPrimitive(""),
        new JsonPrimitive("\""),
        new JsonPrimitive("\n"),
        new JsonPrimitive("foo"),
        new JsonPrimitive("bar"),
        new JsonPrimitive("Hello, world!"),
        new JsonPrimitive("The fox jumped over the lazy dogs."),

        new JsonArray(0),
        new JsonObject(),
    };

    /**
     * Reads the {@code fabric.mod.json}, if any, as json test data.
     *
     * @return an array containing the {@code fabric.mod.json} json, empty if none
     */
    private static JsonElement[] fabricModJson() {
        {
            final var self = fabricModJson.get();
            if (self != null) {
                return self;
            }
        }

        var self = NULL_JSON_TEST_DATA;
        final var resource = JsonUtilsTest.class.getClassLoader().getResourceAsStream("fabric.mod.json");
        if (resource != null) {
            try (final var reader = new InputStreamReader(resource)) {
                self = new JsonElement[] { JsonParser.parseReader(reader) };
            } catch (final IOException cause) {
                throw new UncheckedIOException(cause);
            } catch (final JsonParseException ignored) {}
        }
        fabricModJson = new WeakReference<>(self);
        return self;
    }

    /**
     * An empty json test data. This should not add more parameters to test with.
     */
    private static final JsonElement[] NULL_JSON_TEST_DATA = new JsonElement[0];

    /**
     * Weakly saved {@code fabric.mod.json} json to prevent redoing I/O but also not
     * potentially using too much memory when unused.
     */
    private static WeakReference<JsonElement[]> fabricModJson = new WeakReference<>(null);

    /**
     * Tests the serialization scopes inequality. They must not be equal.
     *
     * @see JsonUtils#SERIALIZATION_SCOPE_ARRAY
     * @see JsonUtils#SERIALIZATION_SCOPE_OBJECT
     */
    @Test
    void testSerializationScopeConstantsInequality() {
        //noinspection ConstantValue
        assertNotEquals(JsonUtils.SERIALIZATION_SCOPE_ARRAY, JsonUtils.SERIALIZATION_SCOPE_OBJECT);
    }

    /**
     * Tests {@link JsonUtils#serializeNameIfApplicable}.
     *
     * @throws IOException if an I/O related issue occurred
     */
    @Test
    void testSerializingNameIfApplicable() throws IOException {
        final var keys = new ReferenceArrayList<String>(2);
        final var scopes = new BooleanArrayList(3);

        //noinspection UnnecessarySemicolon
        try (
            final var stringWriter = new StringWriter();
            final var jsonWriter = new JsonWriter(stringWriter);
        ) {
            jsonWriter.beginArray();
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_ARRAY);

            JsonUtils.serializeNameIfApplicable(jsonWriter, keys, scopes);
            assertEquals(0, keys.size());
            jsonWriter.beginObject();
            //noinspection ConstantValue
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_OBJECT);
            keys.push("key1");
            keys.push("key0");

            JsonUtils.serializeNameIfApplicable(jsonWriter, keys, scopes);
            assertEquals(1, keys.size());
            jsonWriter.beginArray();
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_ARRAY);
            jsonWriter.endArray();
            scopes.popBoolean();

            JsonUtils.serializeNameIfApplicable(jsonWriter, keys, scopes);
            assertEquals(0, keys.size());
            jsonWriter.beginObject();
            //noinspection ConstantValue
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_OBJECT);
            jsonWriter.endObject();
            scopes.popBoolean();

            jsonWriter.endObject();
            scopes.popBoolean();

            jsonWriter.endArray();
            scopes.popBoolean();

            assertEquals("[{\"key0\":[],\"key1\":{}}]", stringWriter.toString());
        }
    }

    /**
     * Tests {@link JsonUtils#serializeAndExtractMembers}.
     *
     * @throws IOException if an I/O related issue occurred
     */
    @Test
    void testSerializingAndExtractingMembers() throws IOException {
        var expectedNodes = new ReferenceArrayList<JsonElement>(6);
        expectedNodes.push(new JsonObject());
        expectedNodes.push(new JsonArray());
        expectedNodes.push(new JsonPrimitive(""));
        expectedNodes.push(new JsonPrimitive(0));
        expectedNodes.push(new JsonPrimitive(false));
        expectedNodes.push(JsonNull.INSTANCE);

        final var keys = new ReferenceArrayList<String>(0);
        final var markers = new IntArrayList(2);
        final var scopes = new BooleanArrayList(2);
        final var nodes = new ReferenceArrayList<JsonElement>(expectedNodes.size());

        //noinspection UnnecessarySemicolon
        try (
            final var stringWriter = new StringWriter();
            final var jsonWriter = new JsonWriter(stringWriter);
        ) {
            jsonWriter.beginArray();
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_ARRAY);
            markers.push(nodes.size());
            nodes.addAll(expectedNodes);

            while (expectedNodes.size() > 2) {
                JsonUtils.serializeAndExtractMembers(jsonWriter, keys, markers, scopes, nodes, nodes.pop());
                expectedNodes.pop();
                assertEquals(expectedNodes, nodes);
            }

            JsonUtils.serializeAndExtractMembers(jsonWriter, keys, markers, scopes, nodes, nodes.pop());
            jsonWriter.endArray();
            expectedNodes.pop();
            assertEquals(expectedNodes, nodes);
            assertEquals(nodes.size(), markers.popInt());
            assertEquals(JsonUtils.SERIALIZATION_SCOPE_ARRAY, scopes.popBoolean());

            JsonUtils.serializeAndExtractMembers(jsonWriter, keys, markers, scopes, nodes, nodes.pop());
            jsonWriter.endObject();
            expectedNodes.pop();
            assertEquals(expectedNodes, nodes);
            assertEquals(nodes.size(), markers.popInt());
            //noinspection ConstantValue
            assertEquals(JsonUtils.SERIALIZATION_SCOPE_OBJECT, scopes.popBoolean());

            jsonWriter.endArray();
            assertEquals("[null,false,0,\"\",[],{}]", stringWriter.toString());
        }
    }

    /**
     * Tests {@link JsonUtils#serializeScopeEndIfApplicable}.
     *
     * @throws IOException if an I/O related issue occurred
     */
    @Test
    void testSerializingScopeEndIfApplicable() throws IOException {
        class Nodes extends AbstractReferenceList<JsonElement> {
            public int size;

            @Override
            public int size() {
                return this.size;
            }

            @Override
            public JsonElement get(int index) {
                throw new UnsupportedOperationException("get");
            }
        }

        final var markers = new IntArrayList();
        final var scopes = new BooleanArrayList();
        final var nodes = new Nodes();

        //noinspection UnnecessarySemicolon
        try (
            final var stringWriter = new StringWriter();
            final var jsonWriter = new JsonWriter(stringWriter);
        ) {
            jsonWriter.beginArray();
            markers.push(nodes.size);
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_ARRAY);
            nodes.size += 2;
            JsonUtils.serializeScopeEndIfApplicable(jsonWriter, markers, scopes, nodes);
            assertEquals(IntList.of(0), markers);

            nodes.size--;
            jsonWriter.beginArray();
            markers.push(nodes.size);
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_ARRAY);
            JsonUtils.serializeScopeEndIfApplicable(jsonWriter, markers, scopes, nodes);
            assertEquals(IntList.of(0), markers);

            nodes.size--;
            jsonWriter.beginObject();
            markers.push(nodes.size);
            //noinspection ConstantValue
            scopes.push(JsonUtils.SERIALIZATION_SCOPE_OBJECT);
            JsonUtils.serializeScopeEndIfApplicable(jsonWriter, markers, scopes, nodes);
            assertEquals(IntList.of(), markers);

            assertEquals("[[],{}]", stringWriter.toString());
        }
    }


    /**
     * Tests {@link JsonUtils#extractArrayElementsForSerialization}.
     */
    @Test
    void testExtractingArrayElementsForSerialization() {
        class Nodes extends ReferenceArrayList<JsonElement> {
            public Nodes(final int capacity) {
                super(capacity);
            }

            @Override
            public int size() {
                return GHOST_NODE_COUNT + size;
            }

            @Override
            public JsonElement peek(int i) {
                return get(size - i - 1);
            }
        }

        final var markers = new IntArrayList(1);
        final var scopes = new BooleanArrayList(1);
        final var nodes = new Nodes(EXTRACT_COUNT);
        final var node = new JsonArray(EXTRACT_COUNT);
        for (var i = 0; i < EXTRACT_COUNT; i++) {
            node.add(i);
        }

        JsonUtils.extractArrayElementsForSerialization(markers, scopes, nodes, node);
        assertEquals(IntList.of(GHOST_NODE_COUNT), markers);
        assertEquals(BooleanList.of(JsonUtils.SERIALIZATION_SCOPE_ARRAY), scopes);
        assertEquals(GHOST_NODE_COUNT + EXTRACT_COUNT, nodes.size());
        for (var i = 0; i < EXTRACT_COUNT; i++) {
            assertEquals(i, nodes.peek(i).getAsInt());
        }
    }

    /**
     * Tests {@link JsonUtils#extractObjectEntriesForSerialization}.
     */
    @Test
    void testExtractingObjectEntriesForSerialization() {
        class Nodes extends ReferenceArrayList<JsonElement> {
            public Nodes(final int capacity) {
                super(capacity);
            }

            @Override
            public int size() {
                return GHOST_NODE_COUNT + size;
            }

            @Override
            public JsonElement peek(int i) {
                return get(size - i - 1);
            }
        }

        final var keys = new ReferenceArrayList<String>(EXTRACT_COUNT);
        final var markers = new IntArrayList(1);
        final var scopes = new BooleanArrayList(1);
        final var nodes = new Nodes(EXTRACT_COUNT);
        final var node = new JsonObject();
        for (var i = 0; i < EXTRACT_COUNT; i++) {
            node.addProperty("property" + i, i * EXTRACT_COUNT);
        }

        JsonUtils.extractObjectEntriesForSerialization(keys, markers, scopes, nodes, node);
        assertEquals(IntList.of(GHOST_NODE_COUNT), markers);
        //noinspection ConstantValue
        assertEquals(BooleanList.of(JsonUtils.SERIALIZATION_SCOPE_OBJECT), scopes);
        for (var i = 0; i < EXTRACT_COUNT; i++) {
            assertEquals("property" + i, keys.peek(i));
        }
        assertEquals(GHOST_NODE_COUNT + EXTRACT_COUNT, nodes.size());
        for (var i = 0; i < EXTRACT_COUNT; i++) {
            assertEquals(i * EXTRACT_COUNT, nodes.peek(i).getAsInt());
        }
    }

    /**
     * The number of ghost or non-existent elements that increase the node count.
     */
    private static final int GHOST_NODE_COUNT = ThreadLocalRandom.current().nextInt(0, 256);

    /**
     * The amount of members used in extraction testing for both arrays and objects.
     */
    private static final int EXTRACT_COUNT = 5;
}

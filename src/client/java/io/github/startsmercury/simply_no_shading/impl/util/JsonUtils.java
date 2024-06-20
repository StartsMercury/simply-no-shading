package io.github.startsmercury.simply_no_shading.impl.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;
import it.unimi.dsi.fastutil.objects.AbstractReferenceList;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;

/**
 * Helper methods to handle JSONs.
 *
 * @since 7.1.0
 */
public final class JsonUtils {
    /**
     * Serializes a json element.
     *
     * @param writer the writer
     * @param node the json element
     * @throws IOException if there is a problem performing I/O operations
     */
    public static void serialize(
        final JsonWriter writer,
        final JsonElement node
    ) throws IOException {
        final var keys = new ReferenceArrayList<String>();
        final var markers = new IntArrayList();
        final var scopes = new BooleanArrayList();
        final var nodes = new ReferenceArrayList<JsonElement>();

        serializeAndExtractMembers(writer, keys, markers, scopes, nodes, node);
        serializeScopeEndIfApplicable(writer, markers, scopes, nodes);

        while (!nodes.isEmpty()) {
            serializeNameIfApplicable(writer, keys, scopes);
            serializeAndExtractMembers(writer, keys, markers, scopes, nodes, nodes.pop());
            serializeScopeEndIfApplicable(writer, markers, scopes, nodes);
        }
    }

    /**
     * The current node is a type of array: it has a sequence of elements.
     *
     * @since 7.4.0
     */
    @VisibleForTesting
    static final boolean SERIALIZATION_SCOPE_ARRAY = false;

    /**
     * The current node is a type of object; it has a sequence of named elements.
     *
     * @since 7.4.0
     */
    @VisibleForTesting
    static final boolean SERIALIZATION_SCOPE_OBJECT = !SERIALIZATION_SCOPE_ARRAY;

    /**
     * Serializes the name of the named property.
     *
     * @param writer the writer accepts prepared fragments of the json tree
     * @param keys the key stack collects the names of object properties. Missing
     *     keys for named properties will result in a runtime exception.
     * @param scopes the scope stack determines if the current node is either an
     *     array or an object encoded as boolean false and true respectively
     * @throws IOException if there is a problem performing I/O operations
     * @since 7.4.0
     * @see #serialize
     */
    @VisibleForTesting
    static void serializeNameIfApplicable(
        final JsonWriter writer,
        final Stack<String> keys,
        final BooleanStack scopes
    ) throws IOException {
        // ASSERTION: scopes is not empty
        if (scopes.topBoolean() == SERIALIZATION_SCOPE_OBJECT) {
            // NOTE: there should be enough keys in an OBJECT scope, which
            //       was extracted from a JsonObject along with their nodes
            writer.name(keys.pop());
        }
    }

    /**
     * Serializes a node if it is a value type; extracts and queues up its members
     * if it is either an array or an object.
     *
     * @param writer the writer accepts prepared fragments of the json tree
     * @param keys the key stack collects the names of object properties. Missing
     *     keys for named properties will result in a runtime exception.
     * @param markers the marker stack determines the depth boundaries in the
     *     flatten json tree
     * @param scopes the scope stack determines if the current node is either an
     *     array or an object encoded as boolean false and true respectively
     * @param nodes the node stack is a flatten representation of the json tree
     * @param node the current node, usually a member of the node stack
     * @throws IOException if there is a problem performing I/O operations
     * @since 7.4.0
     * @see #serialize
     */
    @VisibleForTesting
    static void serializeAndExtractMembers(
        final JsonWriter writer,
        final Stack<String> keys,
        final IntStack markers,
        final BooleanStack scopes,
        final AbstractReferenceList<JsonElement> nodes,
        final JsonElement node
    ) throws IOException {
        switch (node) {
            case null -> writer.nullValue();
            case final JsonNull ignored -> writer.nullValue();
            case final JsonPrimitive jsonPrimitive when jsonPrimitive.isBoolean() ->
                writer.value(jsonPrimitive.getAsBoolean());
            case final JsonPrimitive jsonPrimitive when jsonPrimitive.isNumber() ->
                writer.value(jsonPrimitive.getAsNumber());
            case final JsonPrimitive jsonPrimitive -> writer.value(jsonPrimitive.getAsString());
            case final JsonArray jsonArray -> {
                writer.beginArray();
                extractArrayElementsForSerialization(markers, scopes, nodes, jsonArray);
            }
            case final JsonObject jsonObject -> {
                writer.beginObject();
                extractObjectEntriesForSerialization(keys, markers, scopes, nodes, jsonObject);
            }
            default -> throw new IllegalArgumentException(
                "Unrecognized node type: " + node.getClass().getName()
            );
        }
    }

    /**
     * Serializes every scope end of similar markers if any are reached. Multiple
     * markers represents a "last-chain" for multiple scopes.
     *
     * @param writer the writer accepts prepared fragments of the json tree
     * @param markers the marker stack determines the depth boundaries in the
     *     flatten json tree
     * @param scopes the scope stack determines if the current node is either an
     *     array or an object encoded as boolean false and true respectively
     * @param nodes the node stack is a flatten representation of the json tree
     * @throws IOException if there is a problem performing I/O operations
     * @since 7.4.0
     * @see #serialize
     */
    @VisibleForTesting
    static void serializeScopeEndIfApplicable(
        final JsonWriter writer,
        final IntStack markers,
        final BooleanStack scopes,
        final AbstractReferenceList<JsonElement> nodes
    ) throws IOException {
        final var cursor = nodes.size();
        while (!markers.isEmpty() && markers.topInt() == cursor) {
            // Make an exception occur earlier if any
            // NOTE: there should be as many scopes as there are markers
            if (scopes.popBoolean() == SERIALIZATION_SCOPE_ARRAY) {
                writer.endArray();
            } else {
                writer.endObject();
            }

            markers.popInt();
        }
    }

    /**
     * Extracts the elements of this array for serialization
     *
     * @param markers the marker stack determines the depth boundaries in the
     *     flatten json tree
     * @param scopes the scope stack determines if the current node is either an
     *     array or an object encoded as boolean false and true respectively
     * @param nodes the node stack is a flatten representation of the json tree
     * @param node the current node, usually a member of the node stack
     * @since 7.4.0
     * @see #serializeAndExtractMembers
     */
    @VisibleForTesting
    static void extractArrayElementsForSerialization(
        final IntStack markers,
        final BooleanStack scopes,
        final AbstractReferenceList<JsonElement> nodes,
        final JsonArray node
    ) {

        markers.push(nodes.size());
        scopes.push(SERIALIZATION_SCOPE_ARRAY);

        for (var i = node.size() - 1; i >= 0; i--) {
            nodes.push(node.get(i));
        }
    }

    /**
     * Extracts the key-value pair entries of this object for serialization.
     *
     * @param keys the key stack collects the names of object properties. Missing
     *     keys for named properties will result in a runtime exception.
     * @param markers the marker stack determines the depth boundaries in the
     *     flatten json tree
     * @param scopes the scope stack determines if the current node is either an
     *     array or an object encoded as boolean false and true respectively
     * @param nodes the node stack is a flatten representation of the json tree
     * @param node the current node, usually a member of the node stack
     * @since 7.4.0
     * @see #serializeAndExtractMembers
     */
    @VisibleForTesting
    static void extractObjectEntriesForSerialization(
        final Stack<String> keys,
        final IntStack markers,
        final BooleanStack scopes,
        final AbstractReferenceList<JsonElement> nodes,
        final JsonObject node
    ) {
        markers.push(nodes.size());
        //noinspection ConstantValue
        scopes.push(SERIALIZATION_SCOPE_OBJECT);

        var idx = node.size();
        final var extractedKeys = new String[idx];
        final var extractedNodes = new JsonElement[idx];

        idx--;
        for (final var entry : node.entrySet()) {
            extractedKeys[idx] = entry.getKey();
            extractedNodes[idx] = entry.getValue();
            idx--;
        }

        for (final var extractedKey : extractedKeys) {
            keys.push(extractedKey);
        }
        for (final var extractedNode : extractedNodes) {
            nodes.push(extractedNode);
        }
    }

    private JsonUtils() {}
}

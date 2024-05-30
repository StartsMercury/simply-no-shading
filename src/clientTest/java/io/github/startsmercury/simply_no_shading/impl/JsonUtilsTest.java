package io.github.startsmercury.simply_no_shading.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import io.github.startsmercury.simply_no_shading.impl.util.JsonUtils;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

/**
 * @since 7.0.0
 */
class JsonUtilsTest {
    static void assertSerialization(final JsonElement value, final String expected) {
        final var stringWriter = new StringWriter();
        final var jsonWriter = new JsonWriter(stringWriter);
        assertDoesNotThrow(() -> JsonUtils.serialize(jsonWriter, value));

        final var actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    void empty() {
        assertSerialization(new JsonArray(0), "[]");
        assertSerialization(new JsonObject(), "{}");
    }

    @Test
    void simple_nesting() {
        final var arrWithObj = new JsonArray(1);
        arrWithObj.add(new JsonObject());
        assertSerialization(arrWithObj, "[{}]");

        
        final var objWithArr = new JsonObject();
        objWithArr.add("0", new JsonArray(0));
        assertSerialization(objWithArr, "{\"0\":[]}");
    }
}

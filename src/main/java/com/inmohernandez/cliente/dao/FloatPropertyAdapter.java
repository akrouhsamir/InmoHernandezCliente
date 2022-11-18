package com.inmohernandez.cliente.dao;

import com.google.gson.*;
import javafx.beans.property.*;
import java.lang.reflect.Type;

public class FloatPropertyAdapter implements
        JsonSerializer<FloatProperty>,
        JsonDeserializer<FloatProperty> {
    @Override
    public JsonElement serialize(
            FloatProperty property,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return new JsonPrimitive(
                property.getValue()
        );
    }

    @Override
    public FloatProperty deserialize(
            JsonElement json,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return new SimpleFloatProperty(
                json.getAsJsonPrimitive().getAsFloat()
        );
    }
}
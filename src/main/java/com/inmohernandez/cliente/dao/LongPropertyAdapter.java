package com.inmohernandez.cliente.dao;

import com.google.gson.*;
import javafx.beans.property.*;
import java.lang.reflect.Type;

public class LongPropertyAdapter implements
        JsonSerializer<LongProperty>,
        JsonDeserializer<LongProperty> {
    @Override
    public JsonElement serialize(
            LongProperty property,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return new JsonPrimitive(
                property.getValue()
        );
    }

    @Override
    public LongProperty deserialize(
            JsonElement json,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return new SimpleLongProperty(
                json.getAsJsonPrimitive().getAsLong()
        );
    }
}
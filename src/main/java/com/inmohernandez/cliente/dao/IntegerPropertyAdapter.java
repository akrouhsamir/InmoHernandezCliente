package com.inmohernandez.cliente.dao;

import com.google.gson.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


import java.lang.reflect.Type;

public class IntegerPropertyAdapter implements
        JsonSerializer<IntegerProperty>,
        JsonDeserializer<IntegerProperty> {
    @Override
    public JsonElement serialize(
            IntegerProperty property,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return new JsonPrimitive(
                property.getValue()
        );
    }

    @Override
    public IntegerProperty deserialize(
            JsonElement json,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return new SimpleIntegerProperty(
                json.getAsJsonPrimitive().getAsInt()
        );
    }
}
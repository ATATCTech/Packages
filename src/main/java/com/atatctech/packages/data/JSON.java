package com.atatctech.packages.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JSON extends Cloneable {
    ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    static @NotNull String stringify(@Nullable Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ignored) {
            return "";
        }
    }
    static <T> @Nullable T parseString(@Nullable String json, @NotNull Class<T> classOf) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, classOf);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    static <T> @Nullable T parseString(@Nullable String json, @NotNull TypeReference<T> valueTypeRef) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    static <T> @Nullable T parseString(@Nullable String json, @NotNull JavaType valueType) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    default @NotNull String stringify() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException ignored) {
            return "";
        }
    }
}

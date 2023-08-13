package com.atatctech.packages.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class JSON implements Cloneable {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public @NotNull JSON clone() {
        try {
            return (JSON) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static @NotNull String stringify(@Nullable Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ignored) {
            return "";
        }
    }
    public static <T> @Nullable T parseString(@Nullable String json, @NotNull Class<T> classOf) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, classOf);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    public static <T> @Nullable T parseString(@Nullable String json, @NotNull TypeReference<T> valueTypeRef) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> @Nullable T parseString(@Nullable String json, @NotNull JavaType valueType) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public @NotNull String stringify() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException ignored) {
            return "";
        }
    }
}

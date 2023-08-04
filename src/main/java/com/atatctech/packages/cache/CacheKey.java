package com.atatctech.packages.cache;

import com.atatctech.packages.log.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class CacheKey {
    protected final Object index;
    protected @NotNull Log.Time birth = new Log.Time();
    public CacheKey(@NotNull Object index) {
        this.index = index;
    }

    public CacheKey(@NotNull String index) {
        this.index = index;
    }

    public CacheKey(@NotNull String @NotNull ... keywords) {
        index = Arrays.asList(keywords);
    }

    public void rebirth() {
        birth = new Log.Time();
    }

    public long age() {
        return birth.getDuration();
    }

    public @NotNull Log.Time.Milliseconds ageAsGap() {
        return birth.getDurationAsGap();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheKey key)) return false;
        return Objects.equals(index, key.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}

package com.atatctech.packages.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheContainer<T> {
    @FunctionalInterface
    public interface Filter {
        boolean drop(@NotNull CacheKey key, @NotNull Cache<?> value);
    }
    protected final Map<CacheKey, Cache<T>> cacheMap = new ConcurrentHashMap<>();
    protected Filter filter = (key, value) -> false;

    public CacheContainer() {
    }

    synchronized public void abandonOldest(int n) {
        List<CacheKey> list = cacheMap.keySet().stream().sorted(Comparator.comparing(CacheKey::age)).toList().subList(0, n);
        for (CacheKey key : list) cacheMap.remove(key);
    }

    synchronized public void inspect() {
        cacheMap.forEach((k, v) -> {
            if (v.hasExpired() || filter.drop(k, v)) cacheMap.remove(k);
        });
    }

    public boolean containsKey(@NotNull CacheKey key) {
        return cacheMap.containsKey(key);
    }

    public boolean containsValue(@NotNull Cache<?> value) {
        return cacheMap.containsValue(value);
    }

    public boolean contains(@NotNull CacheKey key) {
        return containsKey(key);
    }

    public void put(@NotNull CacheKey key, @NotNull Cache<T> value) {
        inspect();
        key.rebirth();
        cacheMap.put(key, value);
    }

    public void put(@NotNull Object key, @NotNull Cache<T> value) {
        put(new CacheKey(key), value);
    }

    public @Nullable Cache<T> get(@NotNull CacheKey key) {
        inspect();
        return cacheMap.get(key);
    }

    public @Nullable T getCache(@NotNull CacheKey key) {
        Cache<T> cache = get(key);
        return cache == null ? null : cache.getCache();
    }

    @Override
    public String toString() {
        return cacheMap.toString();
    }
}

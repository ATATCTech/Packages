package com.atatctech.packages.cache;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheContainer<T> {
    @FunctionalInterface
    public interface Filter {
        boolean drop(CacheKey key, Cache<?> value);
    }
    protected Map<CacheKey, Cache<T>> cacheMap = new ConcurrentHashMap<>();
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

    public boolean containsKey(CacheKey key) {
        return cacheMap.containsKey(key);
    }

    public boolean containsValue(Cache<?> value) {
        return cacheMap.containsValue(value);
    }

    public boolean contains(CacheKey key) {
        return containsKey(key);
    }

    public void put(@NotNull CacheKey key, Cache<T> value) {
        inspect();
        key.rebirth();
        cacheMap.put(key, value);
    }

    public void put(Object key, Cache<T> value) {
        put(new CacheKey(key), value);
    }

    public void put(Object key, Object value) {
        put(new CacheKey(key), new Cache<>(value));
    }

    public Cache<T> get(CacheKey key) {
        inspect();
        return cacheMap.get(key);
    }

    public T getCache(CacheKey key) {
        return get(key).getCache();
    }

    @Override
    public String toString() {
        return cacheMap.toString();
    }
}

package com.atatctech.packages.cache;

import com.atatctech.packages.log.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Cache<T> {
    protected @NotNull Log.Time expire = Log.Time.FUTURE;
    protected @Nullable Log.Time.TimePeriod refreshInterval;
    private final @NotNull T object;

    public Cache(@NotNull T object) {
        this.object = object;
    }

    public Cache(@NotNull T object, @NotNull Log.Time.TimePeriod ttl) {
        this.object = object;
        setTTL(ttl);
    }

    public Cache(@NotNull T object, @NotNull Log.Time expire) {
        this.object = object;
        setExpire(expire);
    }

    public void setExpire(@NotNull Log.Time expire) {
        this.expire = expire;
    }

    public void setTTL(@NotNull Log.Time.TimePeriod ttl) {
        setExpire(new Log.Time().forward(ttl));
    }

    public void setRefreshInterval(@Nullable Log.Time.TimePeriod refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public @Nullable Log.Time.TimePeriod getRefreshInterval() {
        return refreshInterval;
    }

    public @NotNull Log.Time getExpire() {
        return expire;
    }

    public @NotNull T getObject() {
        return object;
    }

    public @NotNull T getCache() {
        return getObject();
    }

    public @NotNull Class<?> getObjectClass() {
        return object.getClass();
    }

    public boolean hasExpired() {
        return expire.isPast();
    }
}

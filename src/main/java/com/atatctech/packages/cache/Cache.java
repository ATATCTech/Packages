package com.atatctech.packages.cache;

import com.atatctech.packages.log.Log;

public class Cache<T> {
    protected Log.Time expire = Log.Time.FUTURE;
    protected Log.Time.TimePeriod refreshInterval;
    private final T object;

    public Cache(T object) {
        this.object = object;
    }

    public Cache(T object, Log.Time.TimePeriod ttl) {
        this.object = object;
        setTTL(ttl);
    }

    public Cache(T object, Log.Time expire) {
        this.object = object;
        setExpire(expire);
    }

    public void setExpire(Log.Time expire) {
        this.expire = expire;
    }

    public void setTTL(Log.Time.TimePeriod ttl) {
        setExpire(new Log.Time().forward(ttl));
    }

    public void setRefreshInterval(Log.Time.TimePeriod refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public Log.Time.TimePeriod getRefreshInterval() {
        return refreshInterval;
    }

    public Log.Time getExpire() {
        return expire;
    }

    public T getObject() {
        return object;
    }

    public T getCache() {
        return getObject();
    }

    public Class<?> getObjectClass() {
        return object.getClass();
    }

    public boolean hasExpired() {
        return expire.isPast();
    }
}

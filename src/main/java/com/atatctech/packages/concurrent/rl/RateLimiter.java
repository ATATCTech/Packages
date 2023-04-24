package com.atatctech.packages.concurrent.rl;

import com.atatctech.packages.concurrent.rl.exception.MaxRate;
import com.atatctech.packages.log.Log;
import org.jetbrains.annotations.NotNull;

public abstract class RateLimiter {
    protected Log.Time lastReplenishment = new Log.Time();
    protected final long unitAmount, interval;
    protected final Bucket bucket;

    public RateLimiter(long unitAmount, long interval) {
        this.unitAmount = unitAmount;
        this.interval = interval;
        bucket = new Bucket(unitAmount, unitAmount);
    }

    public RateLimiter(long unitAmount, Log.Time.@NotNull TimePeriod interval) {
        this.unitAmount = unitAmount;
        this.interval = interval.getMilliseconds();
        bucket = new Bucket(unitAmount, unitAmount);
    }

    protected void updateReplenishmentTime() {
        lastReplenishment = new Log.Time();
    }

    protected long getReplenishmentAmount() {
        return unitAmount * lastReplenishment.getDuration() / interval;
    }

    public void getToken() throws MaxRate {
        preGetToken();
        bucket.tryPour(1);
        postGetToken();
    }

    protected void preGetToken() {
        bucket.safeReplenish(getReplenishmentAmount());
    }

    protected void postGetToken() {
        updateReplenishmentTime();
    }
}

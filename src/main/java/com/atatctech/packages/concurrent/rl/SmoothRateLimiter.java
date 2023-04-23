package com.atatctech.packages.concurrent.rl;

import com.atatctech.packages.log.Log;

public class SmoothRateLimiter extends RateLimiter {
    public SmoothRateLimiter(long unitAmount, long interval) {
        super(unitAmount, interval);
    }

    public SmoothRateLimiter(long unitAmount, Log.Time.TimePeriod interval) {
        super(unitAmount, interval);
    }

    @Override
    protected long getReplenishmentAmount() {
        return (long) (double) (unitAmount * lastReplenishment.getDuration()) / interval;
    }
}

package com.atatctech.packages.concurrent.rl;

import com.atatctech.packages.concurrent.rl.exception.MaxCapacity;
import com.atatctech.packages.concurrent.rl.exception.MaxRate;

public class Bucket {
    protected final long capacity;
    protected long amountLeft;
    public Bucket(long capacity, long initAmount) {
        this.capacity = capacity;
        amountLeft = initAmount;
    }

    public Bucket(long capacity) {
        this.capacity = capacity;
        amountLeft = 0;
    }

    public long getAmountLeft() {
        return amountLeft;
    }

    synchronized protected void add(long amount) {
        amountLeft = Math.max(0, Math.min(amountLeft + amount, capacity));
    }

    public void safeReplenish(long amount) {
        if (amount < 0) throw new IllegalArgumentException("`amount` must be a positive integer.");
        add(amount);
    }

    public boolean replenish(long amount) {
        if (getAmountLeft() + amount > capacity) return false;
        safeReplenish(amount);
        return true;
    }

    public void tryReplenish(long amount) throws MaxCapacity {
        if (!replenish(amount)) throw new MaxCapacity();
    }

    public void safePour(long amount) {
        if (amount < 0) throw new IllegalArgumentException("`amount` must be a positive integer.");
        add(-amount);
    }

    public boolean pour(long amount) {
        if (getAmountLeft() - amount < 0) return false;
        safePour(amount);
        return true;
    }

    public void tryPour(long amount) throws MaxRate {
        if (!pour(amount)) throw new MaxRate();
    }
}

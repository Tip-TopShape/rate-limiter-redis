package com.TipTop.model;

public enum Tier {
    FREE(100, 10, 1),
    PAID(1000, 50, 1);

    public final int capacity;
    public final double refillRate;
    public final int refillInterval;

    Tier(int capacity, double refillRate, int refillInterval) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.refillInterval = refillInterval;
    }
}

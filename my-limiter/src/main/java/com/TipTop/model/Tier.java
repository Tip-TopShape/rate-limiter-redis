package com.TipTop.model;

public enum Tier {
    FREE(100, 10, 1, 60, 60),
    PAID(1000, 50, 1, 60, 600);

    public final int capacity;
    public final double refillRate;
    public final int refillInterval;

    public final int windowSize;
    public final int maxRequestsPerWindow;

    Tier(int capacity, double refillRate, int refillInterval, int windowsize, int maxRequestsPerWindow) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.refillInterval = refillInterval;
        this.windowSize = windowsize;
        this.maxRequestsPerWindow = maxRequestsPerWindow;
    }
}

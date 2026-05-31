package com.TipTop.model;

public record RateLimiterResult(
        String clientId,
        RateLimiterStatus status,
        double remainningTokens,
        Integer retryAfter) {

}

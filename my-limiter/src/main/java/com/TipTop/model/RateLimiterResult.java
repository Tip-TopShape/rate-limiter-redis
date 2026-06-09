package com.TipTop.model;

import java.util.Optional;

public record RateLimiterResult(
        String clientId,
        RateLimiterStatus status,
        Optional<Double> remainningTokens,
        long retryAfter) {

}

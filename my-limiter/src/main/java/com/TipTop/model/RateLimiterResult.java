package com.TipTop.model;

public record RateLimiterResult(String clientId, RateLimiterResult status, int remainningTokens, Integer retryAfter) {

}

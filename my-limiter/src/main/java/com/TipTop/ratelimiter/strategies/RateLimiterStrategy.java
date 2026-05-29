package com.TipTop.ratelimiter.strategies;

import com.TipTop.model.RateLimiterResult;

import redis.clients.jedis.RedisClient;

public interface RateLimiterStrategy {

    public RateLimiterResult check(String clientId);

}

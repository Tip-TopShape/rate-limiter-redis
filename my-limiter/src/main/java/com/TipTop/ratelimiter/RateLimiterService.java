package com.TipTop.ratelimiter;

import com.TipTop.ratelimiter.strategies.RateLimiterStrategy;

import redis.clients.jedis.RedisClient;

public class RateLimiterService {

    // what do you need?
    // what strat so I can use correct one

    private final RateLimiterStrategy rateLimiterStrategy;

    private final RedisClient redisClient;

    public RateLimiterService(RateLimiterStrategy rateLimiterStrategy, RedisClient redisClient) {
        this.rateLimiterStrategy = rateLimiterStrategy;
        this.redisClient = redisClient;
    }

}

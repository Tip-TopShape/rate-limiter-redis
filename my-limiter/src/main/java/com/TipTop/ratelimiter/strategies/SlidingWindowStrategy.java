package com.TipTop.ratelimiter.strategies;

import org.springframework.beans.factory.annotation.Value;

import com.TipTop.model.RateLimiterResult;

import redis.clients.jedis.RedisClient;

public class SlidingWindowStrategy implements RateLimiterStrategy {

    @Value("${scripts.sliding_window")
    private String slidingWindow;

    private final RedisClient redisClient;

    private final String luaSha;

    public SlidingWindowStrategy(RedisClient redisClient, String luaSha) {
        this.redisClient = redisClient;
        this.luaSha = luaSha;
    }

    public RateLimiterResult check(String clientId, Integer tier) {
        // check using sliding window
        redisClient.evalsha(luaSha);
    }

}

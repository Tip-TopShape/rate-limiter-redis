package com.TipTop.ratelimiter.strategies;

import org.springframework.beans.factory.annotation.Value;

import redis.clients.jedis.RedisClient;

public class TokenBucketStrategy implements RateLimiterStrategy {

    @Value("${scripts.token_bucket")
    private String tokenBucketPath;

    private final RedisClient redisClient;

    private final String luaSha;

    public TokenBucketStrategy(RedisClient redisClient, String luaSha) {
        this.redisClient = redisClient;
        this.luaSha = luaSha;
    }

}

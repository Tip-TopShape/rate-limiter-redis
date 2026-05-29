package com.TipTop.ratelimiter.strategies;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.TipTop.model.ClientRecord;
import com.TipTop.model.RateLimiterResult;

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

    public RateLimiterResult check(String clientId, Integer tier) {
        // check using token bucket

        // redisClient.evalsha(luaSha, null, null)
    }
}

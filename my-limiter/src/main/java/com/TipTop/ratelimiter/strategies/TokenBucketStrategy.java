package com.TipTop.ratelimiter.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.TipTop.model.CheckAttempt;
import com.TipTop.model.RateLimiterResult;
import com.TipTop.model.Tier;

import redis.clients.jedis.RedisClient;

public class TokenBucketStrategy implements RateLimiterStrategy {

    private final RedisClient redisClient;

    private final String luaSha;

    public TokenBucketStrategy(RedisClient redisClient, @Qualifier("stratSha") String luaSha) {
        this.redisClient = redisClient;
        this.luaSha = luaSha;
    }

    public CheckAttempt check(String clientId, Tier tier) {
        // check using token bucket

        long now = System.currentTimeMillis() / 1000;
        List<Object> result = (List<Object>) redisClient.evalsha(luaSha, List.of(String.valueOf(clientId)),
                List.of(
                        String.valueOf(tier.capacity),
                        String.valueOf(tier.refillRate),
                        String.valueOf(tier.refillInterval),
                        String.valueOf(now)));

        boolean allowed = ((Long) result.get(0) == 1L);
        Double tokens = ((Long) result.get(1)).doubleValue();
        Long retryAfter = ((Long) result.get(2));

        return new CheckAttempt(allowed, Optional.of(tokens), retryAfter);

    }
}

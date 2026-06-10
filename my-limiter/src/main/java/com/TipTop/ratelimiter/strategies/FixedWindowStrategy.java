package com.TipTop.ratelimiter.strategies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import com.TipTop.model.CheckAttempt;
import com.TipTop.model.Tier;

import redis.clients.jedis.RedisClient;

public class FixedWindowStrategy implements RateLimiterStrategy {

    @Value("${scripts.fixed_window")
    private String slidingWindow;

    private final RedisClient redisClient;

    private final String luaSha;

    public FixedWindowStrategy(RedisClient redisClient, String luaSha) {
        this.redisClient = redisClient;
        this.luaSha = luaSha;
    }

    public CheckAttempt check(String clientId, Tier tier) {
        // check using sliding window
        List<Object> result = (List<Object>) redisClient.evalsha(
                luaSha,
                List.of(String.valueOf(clientId)),
                List.of(String.valueOf(tier.windowSize)));

        boolean allowed = ((Long) result.get(0) == 1L);
        long retryAfter = ((long) result.get(1));

        return new CheckAttempt(allowed, Optional.empty(), retryAfter);
    }

}

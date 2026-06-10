package com.TipTop.ratelimiter.strategies;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.TipTop.model.CheckAttempt;
import com.TipTop.model.Tier;

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

    public CheckAttempt check(String clientId, Tier tier) {
        // local key = KEYS[1]
        // local window = tonumber(ARGV[1])
        // local timeStamp = tonumber(ARGV[2])
        // local requestId = ARGV[3]
        // local limit = tonumber(ARGV[4])
        String uuid = UUID.randomUUID().toString();
        long now = System.currentTimeMillis() / 1000;
        List<Object> result = (List<Object>) redisClient.evalsha(luaSha, List.of(String.valueOf(clientId)),
                List.of(
                        String.valueOf(tier.windowSize),
                        String.valueOf(now),
                        uuid,
                        String.valueOf(tier.maxRequestsPerWindow)));

        boolean allowed = ((Long) result.get(0) == 1L);
        long retryAfter = ((long) result.get(1));

        return new CheckAttempt(allowed, Optional.empty(), retryAfter);
    }

}

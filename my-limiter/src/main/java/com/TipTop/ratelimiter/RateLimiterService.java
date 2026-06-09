package com.TipTop.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;

import com.TipTop.model.CheckAttempt;
import com.TipTop.model.RateLimiterResult;
import com.TipTop.model.RateLimiterStatus;
import com.TipTop.model.Tier;
import com.TipTop.ratelimiter.strategies.RateLimiterStrategy;

import redis.clients.jedis.RedisClient;

public class RateLimiterService {

    // what do you need?
    // what strat so I can use correct one

    private final RateLimiterStrategy rateLimiterStrategy;

    private final Map<String, Tier> Tiers = new ConcurrentHashMap<>();

    private final RedisClient redisClient;

    private final String eq;

    public RateLimiterService(
            RateLimiterStrategy rateLimiterStrategy,
            RedisClient redisClient,
            @Qualifier("enqueueSha") String enqueueSha) {
        this.rateLimiterStrategy = rateLimiterStrategy;
        this.redisClient = redisClient;
        this.eq = enqueueSha;
    }

    public RateLimiterResult check(String clientId, boolean upgrade) {

        if (!Tiers.containsKey(clientId)) {
            Tiers.put(clientId, !upgrade ? Tier.FREE : Tier.PAID);
        }

        RateLimiterStatus status = RateLimiterStatus.ALLOWED;
        CheckAttempt attempt = rateLimiterStrategy.check(clientId, Tiers.get(clientId));

        if (!attempt.allowed()) {
            // try queue
            status = RateLimiterStatus.DENIED;
        }

        Tier tier = Tiers.get(clientId);
        long retryAfter = (long) Math.ceil(1.0 / tier.refillRate);

        return new RateLimiterResult(
                clientId,
                RateLimiterStatus.ALLOWED,
                attempt.remainningTokens(),
                retryAfter);

    }

}

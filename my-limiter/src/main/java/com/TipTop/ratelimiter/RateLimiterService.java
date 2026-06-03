package com.TipTop.ratelimiter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;

import com.TipTop.config.Constants;
import com.TipTop.model.Tier;
import com.TipTop.model.CheckAttempt;
import com.TipTop.model.RateLimiterResult;
import com.TipTop.model.RateLimiterStatus;
import com.TipTop.ratelimiter.strategies.RateLimiterStrategy;

import redis.clients.jedis.RedisClient;

public class RateLimiterService {

    // what do you need?
    // what strat so I can use correct one

    private final RateLimiterStrategy rateLimiterStrategy;

    private final Map<String, Tier> Tiers = new ConcurrentHashMap<>();

    private final RedisClient redisClient;

    private final String eq;

    private final String dq;

    public RateLimiterService(
            RateLimiterStrategy rateLimiterStrategy,
            RedisClient redisClient,
            @Qualifier("enqueueSha") String enqueueSha,
            @Qualifier("dequeueSha") String dequeueSha) {
        this.rateLimiterStrategy = rateLimiterStrategy;
        this.redisClient = redisClient;
        this.eq = enqueueSha;
        this.dq = dequeueSha;
    }

    public RateLimiterResult check(String clientId, boolean upgrade) {

        if (!Tiers.containsKey(clientId)) {
            Tiers.put(clientId, !upgrade ? Tier.FREE : Tier.PAID);
        }

        CheckAttempt attempt = rateLimiterStrategy.check(clientId, Tiers.get(clientId));
        if (attempt.allowed()) {
            return null;
        }

        return new RateLimiterResult(
                clientId,
                RateLimiterStatus.ALLOWED,
                attempt.remainningTokens(),
                null);

    }

    public RateLimiterStatus enqueue(String clientId, String payload) {
        Tier tier = Tiers.get(clientId);
        Object result = (Object) redisClient.evalsha(
                eq,
                List.of("queue:" + clientId),
                List.of(payload, String.valueOf(tier.capacity)));

        RateLimiterStatus status = ((boolean) result)
                ? RateLimiterStatus.QUEUED
                : RateLimiterStatus.FULL;

        return status;
    }

    public void dequeue(String clientId) {
        // poll

        // check

    }

}

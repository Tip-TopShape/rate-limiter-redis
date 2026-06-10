package com.TipTop.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.TipTop.model.CheckAttempt;
import com.TipTop.model.RateLimiterResult;
import com.TipTop.model.RateLimiterStatus;
import com.TipTop.model.Tier;
import com.TipTop.ratelimiter.strategies.RateLimiterStrategy;

import redis.clients.jedis.RedisClient;

@Service
public class RateLimiterService {

    // what do you need?
    // what strat so I can use correct one

    private final RateLimiterStrategy rateLimiterStrategy;

    private final Map<String, Tier> Tiers = new ConcurrentHashMap<>();

    public RateLimiterService(
            RateLimiterStrategy rateLimiterStrategy) {
        this.rateLimiterStrategy = rateLimiterStrategy;
    }

    public RateLimiterResult check(String clientId, boolean upgrade) {

        if (!Tiers.containsKey(clientId)) {
            Tiers.put(clientId, !upgrade ? Tier.FREE : Tier.PAID);
        }

        RateLimiterStatus status = RateLimiterStatus.ALLOWED;
        CheckAttempt attempt = rateLimiterStrategy.check(clientId, Tiers.get(clientId));
        // System.out.println(attempt.retryAfter(), attempt.remainningTokens());
        if (!attempt.allowed()) {
            // try queue
            status = RateLimiterStatus.DENIED;
        }

        return new RateLimiterResult(
                clientId,
                status,
                attempt.remainningTokens(),
                attempt.retryAfter());

    }

}

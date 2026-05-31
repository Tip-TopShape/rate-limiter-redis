package com.TipTop.ratelimiter.strategies;

import com.TipTop.model.CheckAttempt;
import com.TipTop.model.Tier;

import redis.clients.jedis.RedisClient;

public interface RateLimiterStrategy {

    public CheckAttempt check(String clientId, Tier tier);

}

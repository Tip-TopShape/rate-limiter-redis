package com.TipTop.ratelimiter;

import java.util.HashMap;
import java.util.Map;

import com.TipTop.model.ClientRecord;
import com.TipTop.model.RateLimiterResult;
import com.TipTop.ratelimiter.strategies.RateLimiterStrategy;

import redis.clients.jedis.RedisClient;

public class RateLimiterService {

    // what do you need?
    // what strat so I can use correct one

    private final RateLimiterStrategy rateLimiterStrategy;

    private final Map<String, ClientRecord> clientRecords = new HashMap<>();

    private final RedisClient redisClient;

    public RateLimiterService(RateLimiterStrategy rateLimiterStrategy, RedisClient redisClient) {
        this.rateLimiterStrategy = rateLimiterStrategy;
        this.redisClient = redisClient;
    }

    public RateLimiterResult check(String clientId) {

        if (!clientRecords.containsKey(clientId)) {
            // first time user
            // save info
            // need way of determining if they're tier 2 users
            //
        }

        return rateLimiterStrategy.check(clientId);

    }

    public RateLimiterResult check(String clientId, boolean upgrade) {

        if (!clientRecords.containsKey(clientId)) {
            clientRecords.put(clientId, new ClientRecord(0)); // default is free
        }

        if(upgrade) clientRecords.get(clientId).tier()  

        return rateLimiterStrategy.check(clientId);

    }

}

package com.TipTop.ratelimiter.queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;

import com.TipTop.ratelimiter.RateLimiterService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.clients.jedis.RedisClient;

public class Worker {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final RateLimiterService rateLimiterService;

    private final RedisClient redisClient;

    private final String dq;

    public Worker(
            RedisClient redisClient,
            RateLimiterService rateLimiterService,
            @Qualifier("dequeueSha") String dequeueSha) {
        this.rateLimiterService = rateLimiterService;
        this.redisClient = redisClient;
        this.dq = dequeueSha;
    }

    @PostConstruct
    public void start() {

        scheduler.scheduleAtFixedRate(this::dequeue, 0, 1, TimeUnit.SECONDS);
    }

    private void dequeue() {
        // check if client is not empty
        // pop -> move to safe temp queue
        // proccess request
        Object result = redisClient.evalsha(dq);
    }

    @PreDestroy
    public void end() {
        scheduler.shutdown();
    }
}

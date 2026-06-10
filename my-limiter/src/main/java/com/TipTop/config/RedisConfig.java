package com.TipTop.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.TipTop.ratelimiter.strategies.FixedWindowStrategy;
import com.TipTop.ratelimiter.strategies.RateLimiterStrategy;
import com.TipTop.ratelimiter.strategies.SlidingWindowStrategy;
import com.TipTop.ratelimiter.strategies.TokenBucketStrategy;

import redis.clients.jedis.*;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password}")
    private String password;

    @Value("${ratelimiter.strategy}")
    private String strategy;

    @Bean
    public RedisClient redisClient() {
        return RedisClient.builder()
                .hostAndPort(host, port)
                .clientConfig(DefaultJedisClientConfig.builder()
                        .password(password)
                        .ssl(true)
                        .build())
                .build();
    }

    @Bean
    public RateLimiterStrategy resolveStrategy(RedisClient redisClient, Scripts script) {
        switch (strategy) {
            case "token_bucket":
                return new TokenBucketStrategy(redisClient, script.tokenBucket());
            case "sliding_window":
                return new SlidingWindowStrategy(redisClient, script.slidingWindow());
            case "fixed_window":
                return new FixedWindowStrategy(redisClient, script.fixedWindow());
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }

    @Bean("stratSha")
    public String loadLua(RedisClient redisClient) {
        try {
            String script = new String(getClass()
                    .getResourceAsStream("/scripts/" + strategy + ".lua")
                    .readAllBytes());
            return redisClient.scriptLoad(script);
        } catch (IOException e) {
            throw new IllegalArgumentException("No script for strategy: " + strategy);
        }

    }
}

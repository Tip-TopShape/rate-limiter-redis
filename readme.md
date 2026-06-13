# Rate Limiter — Redis + Lua
 
Distributed rate limiter built with Java 21, Spring Boot 3.2, Jedis 7.2, and Upstash Redis.
 
## Stack
Java 21 · Spring Boot 3.2 · Jedis 7.2 · Upstash Redis (multi-region) · Lua · k6
 
## Algorithms
Swappable via `application.yml`, no code changes needed. All checks are atomic via Lua `EVALSHA`.
 
- **Token Bucket** - lazy refill calculated at request time
- **Sliding Window** — timestamp-based
- **Fixed Window** — counter + TTL

**Token Bucket**
```
p50=89ms  |  p90=170ms  |  p95=193ms
    
throughput =    256 req/s
throttle rate = 76%
```

**Sliding Window**
```
p50=84ms  |  p90=162ms  |  p95=185ms

throughput =    266 req/s
throttle rate = 97%
```

**Fixed Window**
```
p50=86ms  |  p90=161ms  |  p95=184ms

throughput =    269 req/s
throttle rate = 97%
```

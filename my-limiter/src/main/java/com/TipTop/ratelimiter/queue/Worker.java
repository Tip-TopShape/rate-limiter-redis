package com.TipTop.ratelimiter.queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class Worker {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void start() {

        scheduler.scheduleAtFixedRate(this::drain, 0, 1, TimeUnit.SECONDS);
    }

    private void drain() {
        // check if client is not empty
        // pop -> move to safe temp queue
        // proccess request
    }

    @PreDestroy
    public void end() {
        scheduler.shutdown();
    }
}

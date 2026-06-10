package com.TipTop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "scripts.path")
@Component
public record Scripts(String tokenBucket, String slidingWindow, String fixedWindow) {

}

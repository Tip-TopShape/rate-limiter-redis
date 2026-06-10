package com.TipTop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/limiter")
public class RateLimiterController {

    @GetMapping("/hit_me")
    public String test() {
        return "ow";
    }
}

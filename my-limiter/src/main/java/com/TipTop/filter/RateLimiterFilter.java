package com.TipTop.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.TipTop.ratelimiter.RateLimiterService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    public RateLimiterFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse reponse,
            FilterChain chain) throws ServletException, IOException {

        String clientId = request.getHeader("X-Client-Id");

        System.out.println("Incoming request: " + request.getMethod() + " " + request.getRequestURI());
    }
}

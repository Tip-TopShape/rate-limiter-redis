package com.TipTop.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.TipTop.model.RateLimiterResult;
import com.TipTop.model.RateLimiterStatus;
import com.TipTop.ratelimiter.RateLimiterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    public RateLimiterFilter(
            RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String clientId = request.getHeader("X-Client-Id");

        boolean upgrade = false; // may be useless

        RateLimiterResult result = rateLimiterService.check(clientId, upgrade);

        System.out.println("Incoming request: " + request.getMethod() + " " + request.getRequestURI());

        if (result.status().equals(RateLimiterStatus.DENIED)) {
            // try queue
            response.setStatus(429); // TOO_MANY_REQUESTS
            response.getWriter().write("Too Many Requests...");
            return;
        }

        chain.doFilter(request, response);

    }
}
